package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {

    private int id;
    private int transferTypeId;
    private int transferStatusId;
    private String usernameFrom;
    private String usernameTo;
    private BigDecimal amount;
    private String transferStatusName;
    private String transferTypeName;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }


    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }


    public String getTransferStatusName() {
        return transferStatusName;
    }

    public void setTransferStatusName(String transferStatusName) {
        this.transferStatusName = transferStatusName;
    }

    public String getTransferTypeName() {
        return transferTypeName;
    }

    public void setTransferTypeName(String transferTypeName) {
        this.transferTypeName = transferTypeName;
    }

    public String getUsernameFrom() {
        return usernameFrom;
    }

    public void setUsernameFrom(String usernameFrom) {
        this.usernameFrom = usernameFrom;
    }

    public String getUsernameTo() {
        return usernameTo;
    }

    public void setUsernameTo(String usernameTo) {
        this.usernameTo = usernameTo;
    }
}
