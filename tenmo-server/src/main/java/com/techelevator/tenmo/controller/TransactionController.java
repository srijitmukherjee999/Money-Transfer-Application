package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
public class TransactionController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransactionController(JdbcAccountDao jdbcAccountDao, JdbcTransferDao jdbcTransferDao,JdbcUserDao jdbcUserDao){
        this.accountDao = jdbcAccountDao;
        this.transferDao = jdbcTransferDao;
        this.userDao = jdbcUserDao;
    }

    @RequestMapping(path = "/account/balance/{id}", method = RequestMethod.GET)
    public double getAccountBalance(@PathVariable int id){
        try {
            double balance = accountDao.getAccountBalanceById(id);
            return balance;
        }catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account unavailable");
        }

    }

    @RequestMapping(path = "/tenmo_user", method = RequestMethod.GET)
    public List<User> getListOfUsers(){
        return userDao.getUsers();
    }
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/send", method =  RequestMethod.POST)
    public Transfer sendAmount(@Valid @RequestParam(defaultValue = "") String name, @RequestBody Transfer transfer, Principal principal){
        Transfer newTransfer = new Transfer();
        if(transfer.getAmount()>0 && (!principal.getName().equals(name))) {

           User user = userDao.getUserByUsername(name);
           Account account = accountDao.getAccountbyUserId(user.getId());
           account.setIncreasedBalance(transfer.getAmount());
            User userFrom = userDao.getUserByUsername(principal.getName());
            Account accountFrom = accountDao.getAccountbyUserId(userFrom.getId());
            accountFrom.setDecreasedBalance(transfer.getAmount());
            newTransfer = transferDao.createSendTransferByUserName(name, transfer, principal);

        }
        return newTransfer;

    }

    @RequestMapping(path = "/transfer",method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(){
        return transferDao.getListOfTransfers();
    }

    @RequestMapping(path = "/transfer/{id}",method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){

        try {
            return transferDao.getTransferById(id);
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Wrong transaction id");
        }
    }

    //@RequestMapping(path = "/transfer/request", method = RequestMethod.GET)








}
