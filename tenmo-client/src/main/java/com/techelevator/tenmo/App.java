package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import javax.sql.rowset.spi.TransactionalWriter;
import java.math.BigDecimal;
import java.sql.SQLOutput;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private AccountService accountService = new AccountService();

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }else{
            accountService.setToken(currentUser.getToken());
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: " + accountService.getCurrentBalanceByUsername(currentUser.getUser().getUsername()));

	}

	private void viewTransferHistory() {
        System.out.println("1. List of Transfers.");
        System.out.println("2. List of Pending Transfers.");
        int decision = consoleService.promptForInt("Please choose 1 for list of transfers or 2 for pending transfers: ");
        int y = 0;
        if(decision == 1 ) {
            Transfer[] transfers = accountService.listOfTansfersSentOrReceived();
            System.out.println("Here are your list of Transfers: ");

            try {
                for (int i = 0; i < transfers.length; i++) {
                    String output = transfers[i].toString();
                    System.out.println(output);
                    y = 1 ;
                }
            } catch (NullPointerException e) {
                System.out.println("Sorry, there are no transfers");
            }
            if(y == 0){
                System.out.println("Sorry, there are no transfers ");
            }
        } else if(decision == 2) {
            Transfer[] pendingTransfers = accountService.listOfPendingTransfers();
            System.out.println("Here are your list of pending transfers: ");

            int x = 0;
            try {
                for (int i = 0; i < pendingTransfers.length; i++) {
                    String output = pendingTransfers[i].toString();
                    System.out.println(output);
                    x = 1;
                }
            } catch (NullPointerException e) {
                System.out.println("Sorry, there are no pending transfers");
            }
            if (x == 0) {
                System.out.println("Sorry, you have no pending transfers");
            }
        }else{
            System.out.println("Please choose between the two options provided");
        }
    }

	private void viewPendingRequests() {
        Transfer[] transfers = accountService.listOfTransfersToApproveOrReject();
        System.out.println("Here are your list of transfers to approve or reject: ");
        Transfer decisionTransfer = null;
        int success = 2;
        int x = 0;
        try {
            for (int i = 0; i < transfers.length; i++) {
                String output = transfers[i].toString();
                System.out.println(output);
                x = 1;
            }
        } catch (NullPointerException e) {
            System.out.println("Sorry, there are no pending transfers");
        }
        if (x == 0) {
            System.out.println("Sorry, you have no pending transfers");
        } else {
            int id = consoleService.promptForInt("Please choose the transfer ID: ");
            String option = consoleService.promptForString("Please choose the transfer to be approved or rejected: ");
            for (int i = 0; i < transfers.length; i++) {
                if (transfers[i].getId() == id) {
                    decisionTransfer = transfers[i];
                }
            }
            try {
                    decisionTransfer.setTransferStatusName(option);
                    success = accountService.receivedAmount(decisionTransfer);
            } catch (NullPointerException e) {
                System.out.println("The transfer ID doesn't exist");
            }
            if (success == 1) {
                System.out.println("The transfer has been approved!");
            } else if (success == 0){
                System.out.println("The transfer has been rejected");
            }
        }
    }



	private void sendBucks() {
        System.out.println("Here are the list of Users to choose from : ");
       User[] user = accountService.getListOfUsers();

        for (int i = 0; i <user.length ; i++) {
            System.out.println(user[i].getUsername());
        }


        String name	= consoleService.promptForString("Please choose a username from the list: ");
         BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount: ");

        Transfer transfer = new Transfer(0,currentUser.getUser().getUsername(),name, amount, "Approved","Send");

       Transfer receivedTransfer =  accountService.sendAmount(transfer);
        if(receivedTransfer != null) {
            System.out.println(amount + " has been sent to " + name);
        }else{
            System.out.println("Sorry, your transfer didn't go through");
        }
		
	}

	private void requestBucks() {
        System.out.println("Here are the list of Users to choose from : ");
        User[] user = accountService.getListOfUsers();

        for (int i = 0; i <user.length ; i++) {
            System.out.println(user[i].getUsername());
        }


        String name	= consoleService.promptForString("Please choose a username from the list: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount: ");

        Transfer transfer = new Transfer(0,currentUser.getUser().getUsername(),name, amount, "Pending","Request");
          Transfer pendingTransfer =  accountService.pendingAmount(transfer);
          if(pendingTransfer != null) {
              System.out.println("Your request for " + amount + " is pending. " + name + " is waiting to confirm transfer.");
          }else{
              System.out.println("Sorry, your request didn't go through");
          }

	}

}
