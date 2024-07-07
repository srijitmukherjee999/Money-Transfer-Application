package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AccountService {

    private RestTemplate restTemplate = new RestTemplate() ;

    private String url = "http://localhost:8080" ;

    private String token;

    public void setToken(String token) {
        this.token = token;
    }

    public BigDecimal getCurrentBalanceByUsername(String name){
        BigDecimal balance = null;
        try{
            ResponseEntity<BigDecimal> response = restTemplate.exchange(url + "/account/balance?name=" + name, HttpMethod.GET,makeAuthEntity(),BigDecimal.class);
            balance = response.getBody();

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public User[] getListOfUsers(){
        User[] user = null;
        try {
            ResponseEntity<User[]> response = restTemplate.exchange(url + "/tenmo_user", HttpMethod.GET,makeAuthEntity(),User[].class );
            user = response.getBody();

        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return user;
    }


    public Transfer sendAmount(Transfer transfer){
        Transfer returnTransfer = null;

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.APPLICATION_JSON);
        header.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,header);
        try{
            HttpEntity<Transfer> response = restTemplate.exchange(url + "/transfer/send",HttpMethod.POST,entity, Transfer.class);
            returnTransfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return returnTransfer;
    }


    public Transfer[] listOfTansfersSentOrReceived(){
        Transfer[] newTransfer = null;
        try{
           ResponseEntity<Transfer[]> response = restTemplate.exchange(url + "/transfer", HttpMethod.GET,makeAuthEntity(),Transfer[].class);
             newTransfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newTransfer;
    }


    public Transfer getTransferById(int id){
        Transfer transfer = null;
        try{
            ResponseEntity<Transfer> response = restTemplate.exchange(url + "/transfer/" + id,HttpMethod.GET,makeAuthEntity(), Transfer.class);
           transfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public Transfer pendingAmount(Transfer transfer){
        Transfer newTransfer = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);
        try{
           HttpEntity<Transfer> response = restTemplate.exchange(url + "/transfer/request",HttpMethod.POST,entity, Transfer.class);
           newTransfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newTransfer;

    }

    public Transfer[] listOfPendingTransfers(){
        Transfer[] newTransfer = null;
        try{
            ResponseEntity<Transfer[]> response = restTemplate.exchange(url + "/transfer/pending", HttpMethod.GET,makeAuthEntity(),Transfer[].class);
            newTransfer = response.getBody();
        }catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newTransfer;
    }

    public int receivedAmount(Transfer transfer){
      int success = 2;

      HttpHeaders headers = new HttpHeaders();
      headers.setContentType(MediaType.APPLICATION_JSON);
      headers.setBearerAuth(token);
      HttpEntity<Transfer> entity = new HttpEntity<>(transfer,headers);

      try{
         HttpEntity<Integer> response =restTemplate.exchange(url + "/transfer/request",HttpMethod.PUT,entity,int.class);
             success = response.getBody();
      }catch (RestClientResponseException | ResourceAccessException e) {
          BasicLogger.log(e.getMessage());
      }
      return success;
    }



    private HttpEntity<Void> makeAuthEntity(){
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        return new HttpEntity<>(headers);

    }



}
