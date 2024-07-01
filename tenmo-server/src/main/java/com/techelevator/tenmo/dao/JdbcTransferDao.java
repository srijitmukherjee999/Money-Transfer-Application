package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;
    private AccountDao accountDao;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.accountDao = new JdbcAccountDao(jdbcTemplate);
    }


    @Override
    public Transfer getTransferById(int id) {
        Transfer transfer = null;
        String sql = "SELECT * FROM transfer_type JOIN transfer ON transfer_type_id = transfer_id " +
                "JOIN transfer_status ON transfer_id = transfer_status_id WHERE id = ?";

        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
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
    public List<Transfer> getListOfTransfers() {

        List<Transfer> listOfTransfers = new ArrayList<>();
        String sql = "SELECT * FROM transfer_type JOIN transfer ON transfer_type_id = transfer_id " +
                "JOIN transfer_status ON transfer_id = transfer_status_id";
        try{
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
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

    public Transfer mapByRow(SqlRowSet result){
        Transfer transfer = new Transfer();
        transfer.setId(result.getInt("transfer_id"));
        transfer.setAmount(result.getDouble("amount"));
        transfer.setAccountFrom(result.getInt("account_from"));
        transfer.setAccountTo(result.getInt("account_to"));
        transfer.setTransferStatusId(result.getInt("transfer_status_id"));
        transfer.setTransferStatusName(result.getString("transfer_status_desc"));
        transfer.setTransferTypeId(result.getInt("transfer_type_id"));
        transfer.setTransferTypeName(result.getString("transfer_type_desc"));
    }
}
