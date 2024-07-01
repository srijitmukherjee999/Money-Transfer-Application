package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {


    Transfer getTransferById(int id);
    List<Transfer> getListOfTransfers();
    Transfer getTransferbyUserId(int userId);


}
