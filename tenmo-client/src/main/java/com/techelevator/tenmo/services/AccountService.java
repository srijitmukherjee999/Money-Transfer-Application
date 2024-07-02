package com.techelevator.tenmo.services;

import org.springframework.web.client.RestTemplate;

public class AccountService {

    private RestTemplate restTemplate = new RestTemplate() ;

    private String url = "http://localhost8080" ;

    private String token;

    public void setToken(String token) {
        this.token = token;
    }


}
