package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.security.Principal;
import java.util.List;

public interface TransferDao {


    Transfer getTransferById(int id);
    List<Transfer> getListOfTransfers();
    Transfer getTransferbyUserId(int userId);
    Transfer createSendTransferByUserName(String name, Transfer transfer, Principal principal);
    Transfer createReceiveTransferByUserName(String name, Transfer transfer, Principal principal);


}
