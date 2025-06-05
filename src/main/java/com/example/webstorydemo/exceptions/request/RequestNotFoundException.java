package com.example.webstorydemo.exceptions.request;

public class RequestNotFoundException extends RuntimeException{
    public RequestNotFoundException(String s){
        super(s);
    }
}
