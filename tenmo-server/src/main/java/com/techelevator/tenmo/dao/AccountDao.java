package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {

   // Account getAccountbyId();

   // List<Account> getAccounts();
    BigDecimal getAccountBalanceByUsername(String name);
    Account getAccountbyUserId(int userId);
   // Account getAccountbyUserName(String name);

    void updateAccountBalance(Account account);


}
