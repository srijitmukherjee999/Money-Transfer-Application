package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;

import java.security.Principal;
import java.util.List;

public interface TransferDao {


    Transfer getTransferById(int id);
    List<Transfer> getListOfTransfersBySentOrReceived(Principal principal);
    Transfer getTransferbyUserId(int userId);
    Transfer createTransferByUserName(Transfer transfer, Principal principal);


    List<Transfer> getListOfTransfersByPending(Principal principal);
    void updateTransfer(Transfer transfer);
    List<Transfer> getListOfTransfersToApproveOrReject(Principal principal);



}
