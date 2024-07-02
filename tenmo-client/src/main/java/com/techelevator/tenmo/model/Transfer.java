package com.techelevator.tenmo.model;

public class Transfer {


    private String usernameFrom;
    private String usernameTo;
    private double amount;
    private String transferStatusName;
    private String transferTypeName;

    public Transfer(String usernameFrom, String usernameTo, double amount, String transferStatusName, String transferTypeName) {

        this.usernameFrom = usernameFrom;
        this.usernameTo = usernameTo;
        this.amount = amount;
        this.transferStatusName = transferStatusName;
        this.transferTypeName = transferTypeName;
    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
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
