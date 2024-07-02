package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
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
    public Transfer sendAmount(@RequestBody Transfer transfer, Principal principal){
        Transfer newTransfer = new Transfer();
        User userFrom = userDao.getUserByUsername(principal.getName());
        Account accountFrom = accountDao.getAccountbyUserId(userFrom.getId());
        if(transfer.getAmount()>0 && (!principal.getName().equals(transfer.getUsernameTo())) && transfer.getAmount()<= accountFrom.getBalance()) {

           User user = userDao.getUserByUsername(transfer.getUsernameTo());
           Account account = accountDao.getAccountbyUserId(user.getId());
           account.setIncreasedBalance(transfer.getAmount());

            accountFrom.setDecreasedBalance(transfer.getAmount());
            transfer.setTransferStatusId(2);// Approved
            transfer.setTransferTypeId(2); // Sending money
            newTransfer = transferDao.createTransferByUserName(transfer, principal);

        }
        return newTransfer;

    }

    @RequestMapping(path = "/transfer",method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(){
        return transferDao.getListOfTransfersBySentOrReceived();
    }

    @RequestMapping(path = "/transfer/{id}",method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id){

        try {
            return transferDao.getTransferById(id);
        }catch (DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Wrong transaction id");
        }
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public Transfer receiveAmount(@RequestBody Transfer transfer,Principal principal){
        Transfer newTransfer = new Transfer();
        if(transfer.getAmount()>0 && (!principal.getName().equals(transfer.getUsernameTo()))) {
            transfer.setTransferStatusId(1); //Pending
            transfer.setTransferTypeId(1); // Requesting money
            newTransfer = transferDao.createTransferByUserName(transfer,principal);
        }
        return newTransfer;
    }

    @RequestMapping(path = "/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> getListOfPendingTransfers(){

        try {
            return transferDao.getListOfTransfersByPending();

        }catch(DaoException e){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

    }


    @RequestMapping(path = "/transfer/request", method = RequestMethod.PUT)
     public Transfer responseToRequest(@RequestBody Transfer transfer,Principal principal){
        Transfer newTransfer = new Transfer();
        User userFrom = userDao.getUserByUsername(principal.getName());
        Account accountFrom = accountDao.getAccountbyUserId(userFrom.getId());
        if(accountFrom.getBalance() < transfer.getAmount()){
            transfer.setTransferStatusId(3); //Rejected
        }else{
            transfer.setTransferStatusId(2);//Approved
            User userRequester = userDao.getUserByUsername(transfer.getUsernameTo());
            Account accountRequester = accountDao.getAccountbyUserId(userRequester.getId());
            accountRequester.setIncreasedBalance(transfer.getAmount());

            accountFrom.setDecreasedBalance(transfer.getAmount());
            newTransfer = transferDao.createTransferByUserName(transfer,principal);
        }
        return newTransfer;
    }









}
