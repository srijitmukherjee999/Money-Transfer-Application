package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.security.Principal;
import java.util.List;

public interface TransferDao {


    Transfer getTransferById(int id);
    List<Transfer> getListOfTransfersBySentOrReceived();
    Transfer getTransferbyUserId(int userId);
    Transfer createTransferByUserName(Transfer transfer, Principal principal);
    //Transfer createReceiveTransferByUserName(String name, Transfer transfer, Principal principal);

    List<Transfer> getListOfTransfersByPending();


}
