package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.AccountService;
import com.techelevator.tenmo.services.AuthenticationService;
import com.techelevator.tenmo.services.ConsoleService;

import javax.sql.rowset.spi.TransactionalWriter;
import java.math.BigDecimal;

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
		String name = consoleService.promptForString("Please enter your username : ");
        System.out.println("Your current balance is : " + accountService.getCurrentBalanceByUsername(name));

	}

	private void viewTransferHistory() {
	Transfer[] transfers =	accountService.listOfTansfersSentOrReceived();
        System.out.println("Here are your list of transfers: ");

        try {
            for (int i = 0; i < transfers.length; i++) {
                System.out.println(transfers[i]);
            }
        }catch(NullPointerException e){
            System.out.println("Sorry, there are no transactions");
        }
		
	}

	private void viewPendingRequests() {
        Transfer[] transfers =	accountService.listOfPendingTransfers();
        System.out.println("Here are your list of pending transfers: ");
        Transfer decisionTransfer = null;
            int success = -1;
        try {
            for (int i = 0; i < transfers.length; i++) {
                System.out.println(transfers[i].toString());

            }
        }catch(NullPointerException e){
            System.out.println("Sorry, there are no pending transactions");
        }
        int id  = consoleService.promptForInt("Please choose the transfer ID: ");
       String option = consoleService.promptForString("Please choose the transfer to approved or rejected: ");
        for (int i = 0; i < transfers.length ; i++) {
            if(transfers[i].getId() == id){
                decisionTransfer = transfers[i];
            }
        }
        if(option.equalsIgnoreCase("Approved")){
            decisionTransfer.setTransferStatusName(option);
           success =  accountService.receivedAmount(decisionTransfer);
        }else{
            decisionTransfer.setTransferStatusName(option);
            success = accountService.receivedAmount(decisionTransfer);
        }

		if(success == 1){
            System.out.println("The request was successful");
        }else{
            System.out.println("The request has failed");
        }

	}

	private void sendBucks() {
        System.out.println("Here are the list of Users to choose from : ");
        System.out.println(accountService.getListOfUsers());

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
        System.out.println(accountService.getListOfUsers());

        String name	= consoleService.promptForString("Please choose a username from the list: ");
        BigDecimal amount = consoleService.promptForBigDecimal("Please enter the amount: ");

        Transfer transfer = new Transfer(0,currentUser.getUser().getUsername(),name, amount, "Pending","Request");

        System.out.println("Your request for " + amount + " is pending. " + name + " is waiting to confirm transfer." );

	}

}
