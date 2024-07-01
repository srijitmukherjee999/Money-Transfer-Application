package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.util.List;

public interface AccountDao {

   // Account getAccountbyId();

   // List<Account> getAccounts();
    double getAccountBalanceById(int id);
    Account getAccountbyUserId(int userId);
   // Account getAccountbyUserName(String name);


}
