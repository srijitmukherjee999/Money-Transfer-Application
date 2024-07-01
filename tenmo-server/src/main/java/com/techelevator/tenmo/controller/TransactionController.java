package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.*;
import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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




}
