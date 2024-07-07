package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountDao {


    BigDecimal getAccountBalanceByUsername(String name);
    Account getAccountbyUserId(int userId);


    void updateAccountBalance(Account account);


}
