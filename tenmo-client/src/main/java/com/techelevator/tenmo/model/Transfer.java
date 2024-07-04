package com.techelevator.tenmo.model;

import java.math.BigDecimal;

public class Transfer {




    private int id;
    private String usernameFrom;
    private String usernameTo;
    private BigDecimal amount;
    private String transferStatusName;
    private String transferTypeName;

    public Transfer(int id,String usernameFrom, String usernameTo, BigDecimal amount, String transferStatusName, String transferTypeName) {
        this.id = id;
        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.amount = amount;
        this.transferStatusName = transferStatusName;
        this.transferTypeName = transferTypeName;
    }
    public Transfer(){};


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

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "id=" + id +
                ", usernameFrom='" + usernameFrom + '\'' +
                ", usernameTo='" + usernameTo + '\'' +
                ", amount=" + amount +
                ", transferStatusName='" + transferStatusName + '\'' +
                ", transferTypeName='" + transferTypeName + '\'' +
                '}';
    }
}
