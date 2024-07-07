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
import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RestController
@PreAuthorize("isAuthenticated()")
public class TransactionController {

    private AccountDao accountDao;
    private TransferDao transferDao;
    private UserDao userDao;

    public TransactionController(JdbcAccountDao jdbcAccountDao, JdbcTransferDao jdbcTransferDao, JdbcUserDao jdbcUserDao) {
        this.accountDao = jdbcAccountDao;
        this.transferDao = jdbcTransferDao;
        this.userDao = jdbcUserDao;
    }

    @RequestMapping(path = "/account/balance", method = RequestMethod.GET)
    public BigDecimal getAccountBalance(@RequestParam String name) {
        try {
            BigDecimal balance = accountDao.getAccountBalanceByUsername(name);
            return balance;
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Account unavailable");
        }

    }

    @RequestMapping(path = "/tenmo_user", method = RequestMethod.GET)
    public List<User> getListOfUsers() {
        return userDao.getUsers();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(path = "/transfer/send", method = RequestMethod.POST)
    public Transfer sendAmount(@RequestBody Transfer transfer, Principal principal) {
        Transfer newTransfer = null;
        User userFrom = userDao.getUserByUsername(principal.getName());
        Account accountFrom = accountDao.getAccountbyUserId(userFrom.getId());
        if (transfer.getAmount().doubleValue() > 0.00 && (!principal.getName().equals(transfer.getUsernameTo())) && transfer.getAmount().compareTo(accountFrom.getBalance()) <= 0) {

            User user = userDao.getUserByUsername(transfer.getUsernameTo());
            Account account = accountDao.getAccountbyUserId(user.getId());
            account.setIncreasedBalance(transfer.getAmount());
            accountDao.updateAccountBalance(account);//updates account balance in DB

            accountFrom.setDecreasedBalance(transfer.getAmount());
            accountDao.updateAccountBalance(accountFrom);//updates account balance in DB
            transfer.setTransferStatusId(2);// Approved
            transfer.setTransferTypeId(2); // Sending money
            newTransfer = transferDao.createTransferByUserName(transfer, principal);

        } else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }

        return newTransfer;

    }

    @RequestMapping(path = "/transfer", method = RequestMethod.GET)
    public List<Transfer> getListOfTransfers(Principal principal) {
        return transferDao.getListOfTransfersBySentOrReceived(principal);
    }

    @RequestMapping(path = "/transfer/{id}", method = RequestMethod.GET)
    public Transfer getTransferById(@PathVariable int id) {

        try {
            return transferDao.getTransferById(id);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Wrong transaction id");
        }
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public Transfer receiveAmount(@RequestBody Transfer transfer, Principal principal) {           //Pending request
        Transfer newTransfer = new Transfer();
        if (transfer.getAmount().doubleValue() > 0.00 && (!principal.getName().equals(transfer.getUsernameTo()))) {
            transfer.setTransferStatusId(1); //Pending
            transfer.setTransferTypeId(1); // Requesting money
            newTransfer = transferDao.createTransferByUserName(transfer, principal);
        }
        return newTransfer;
    }

    @RequestMapping(path = "/transfer/pending", method = RequestMethod.GET)
    public List<Transfer> getListOfPendingTransfers(Principal principal) {

        try {
            return transferDao.getListOfTransfersByPending(principal);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(path = "/transfer/decision",method = RequestMethod.GET)
    public List<Transfer> getListOfTransfersToApproveOrReject(Principal principal) {

        try {
            return transferDao.getListOfTransfersToApproveOrReject(principal);
        } catch (DaoException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @RequestMapping(path = "/transfer/request", method = RequestMethod.PUT)
     public int updatePendingRequest(@RequestBody Transfer transfer,Principal principal){
        int success = 0;

        User userFrom = userDao.getUserByUsername(principal.getName());
        Account accountFrom = accountDao.getAccountbyUserId(userFrom.getId());


        if (transfer.getTransferStatusName().equalsIgnoreCase("Approved")) {
                if (accountFrom.getBalance().compareTo(transfer.getAmount()) < 0) {
                    transfer.setTransferStatusId(3); //Rejected

                    transferDao.updateTransfer(transfer);

                } else {
                    transfer.setTransferStatusId(2);//Approved
                    User userRequester = userDao.getUserByUsername(transfer.getUsernameTo());
                    Account accountRequester = accountDao.getAccountbyUserId(userRequester.getId());
                    accountRequester.setIncreasedBalance(transfer.getAmount());
                    accountDao.updateAccountBalance(accountRequester);//    Increases the balance in DB

                    accountFrom.setDecreasedBalance(transfer.getAmount());
                    accountDao.updateAccountBalance(accountFrom);//        Decreases the balance in DB
                    transferDao.updateTransfer(transfer);
                    success = 1;
                }
            } else {
                transfer.setTransferStatusId(3); //Rejected

                transferDao.updateTransfer(transfer);
            }


        return success;
    }


}
