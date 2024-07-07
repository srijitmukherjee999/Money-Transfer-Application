package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    @Override
    public BigDecimal getAccountBalanceByUsername(String name) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = (SELECT user_id FROM tenmo_user WHERE username = ? );";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, name);
            while(results.next()){
                account = mapByRow(results);
            }
        }  catch(DataIntegrityViolationException e) {
        throw new DaoException("That park is already in that state!", e);
     } catch (CannotGetJdbcConnectionException e) {
        throw new DaoException("Database not found!", e);
    }
        return account.getBalance();
    }

    @Override
    public Account getAccountbyUserId(int id) {
        Account account = null;
        String sql = "SELECT * FROM account WHERE user_id = ? ;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id);
            while(results.next()){
                account = mapByRow(results);
            }
        }  catch(DataIntegrityViolationException e) {
            throw new DaoException("That park is already in that state!", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database not found!", e);
        }
        return account;

    }
    @Override
    public void updateAccountBalance(Account account){

        String sql = "UPDATE account SET balance = ? WHERE account_id= ? ;";
        jdbcTemplate.update(sql, account.getBalance(),account.getId());
    }










    private Account mapByRow(SqlRowSet result){
        Account account1 = new Account();
        account1.setBalance(result.getBigDecimal("balance"));
        account1.setId(result.getInt("account_id"));
        account1.setUserId(result.getInt("user_id"));
        return account1;
    }
}
