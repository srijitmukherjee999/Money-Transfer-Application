package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;
    private UserDao userDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
        this.userDao = new JdbcUserDao(jdbcTemplate);
    }


    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        String sql = "SELECT transfer_id,transfer_type_id,transfer_status_id,(SELECT username FROM tenmo_user JOIN account USING (user_id) JOIN transfer ON account_id = account_from WHERE transfer_id =?) AS username_from," +
                "(SELECT username FROM tenmo_user JOIN account USING (user_id) JOIN transfer ON account_id = account_to WHERE transfer_id =?) AS username_to,amount, transfer_type_desc,transfer_status_desc " +
                "FROM transfer_type JOIN transfer USING(transfer_type_id) JOIN transfer_status USING(transfer_status_id) WHERE transfer_id = ?;";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,id,id,id);
            while(results.next()){
                transfer = mapByRow(results);
            }
        } catch(DataIntegrityViolationException e) {
            throw new DaoException("That park is already in that state!", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database not found!", e);
        }
        return transfer;
    }

    @Override
    public List<Transfer> getListOfTransfersBySentOrReceived(Principal principal) {

        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id,tbluser_from.username as username_from, tble2tenmo_user.username as username_to,amount, transfer_type_id,transfer_status_id,transfer_status_desc,transfer_type_desc from " +
                "transfer JOIN " +
                "account as tblaccount_from on account_from=tblaccount_from.account_id JOIN " +
                "tenmo_user as tbluser_from on tblaccount_from.user_id=tbluser_from.user_id " +
                "JOIN account as tbl2account_from on account_to = tbl2account_from.account_id " +
                "JOIN tenmo_user AS tble2tenmo_user on tbl2account_from.user_id = tble2tenmo_user.user_id JOIN transfer_type USING(transfer_type_id)" +
                "JOIN transfer_status USING(transfer_status_id) WHERE transfer_status_desc != 'Pending' AND tbluser_from.username = ? ;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,principal.getName());
            while(results.next()){
                listOfTransfers.add(mapByRow(results));
            }
        }catch(DataIntegrityViolationException e) {
            throw new DaoException("That park is already in that state!", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database not found!", e);
        }

        return listOfTransfers;
    }


    @Override
    public Transfer getTransferbyUserId(int id) {
      Account account =  accountDao.getAccountbyUserId(id);
        return getTransferById(account.getId());
    }

    @Override
    public Transfer createTransferByUserName(Transfer transfer,Principal principal) {

       Transfer transfer1 = new Transfer();

      String sql = "INSERT INTO transfer (transfer_type_id , transfer_status_id, account_from,account_to,amount) " +
              "VALUES (?,?,(  SELECT account_id FROM tenmo_user JOIN account USING(user_id) WHERE username = ?), " +
              "(SELECT account_id FROM tenmo_user JOIN account USING (user_id) WHERE username = ?),?)  RETURNING transfer_id ;";
       int update = jdbcTemplate.queryForObject(sql,int.class, transfer.getTransferTypeId(),transfer.getTransferStatusId(),
               principal.getName(),transfer.getUsernameTo(),transfer.getAmount());

       transfer1 = getTransferById(update);
       return transfer1;
    }

    @Override
    public List<Transfer> getListOfTransfersByPending(Principal principal) {

        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "SELECT transfer_id,tbluser_from.username as username_from, tble2tenmo_user.username as username_to,amount, transfer_type_id,transfer_status_id,transfer_status_desc,transfer_type_desc from " +
                "transfer JOIN " +
                "account as tblaccount_from on account_from=tblaccount_from.account_id JOIN " +
                "tenmo_user as tbluser_from on tblaccount_from.user_id=tbluser_from.user_id " +
                "JOIN account as tbl2account_from on account_to = tbl2account_from.account_id " +
                "JOIN tenmo_user AS tble2tenmo_user on tbl2account_from.user_id = tble2tenmo_user.user_id JOIN transfer_type USING(transfer_type_id)" +
                "JOIN transfer_status USING(transfer_status_id) WHERE transfer_status_desc = 'Pending' AND tbluser_from.username = ? ;";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql,principal.getName());
            while(results.next()){
                listOfTransfers.add(mapByRow(results));
            }
        }catch(DataIntegrityViolationException e) {
            throw new DaoException("That park is already in that state!", e);
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Database not found!", e);
        }

        return listOfTransfers;
    }

    public void updateTransfer(Transfer transfer){
        String sql = "UPDATE transfer SET transfer_status_id = ? WHERE transfer_id = ? ;";

        jdbcTemplate.update(sql,transfer.getTransferStatusId(),transfer.getId());
    }


    private Transfer mapByRow(SqlRowSet result){
        Transfer transfer = new Transfer();
        transfer.setId(result.getInt("transfer_id"));
        transfer.setAmount(result.getBigDecimal("amount"));
        transfer.setUsernameFrom(result.getString("username_from"));
        transfer.setUsernameTo(result.getString("username_to"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setTransferStatusName(result.getString("transfer_status_desc"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferTypeName(result.getString("transfer_type_desc"));
        return transfer;
    }
}
