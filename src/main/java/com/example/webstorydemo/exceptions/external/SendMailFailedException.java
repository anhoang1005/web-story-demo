package com.example.webstorydemo.exceptions.external;

public class SendMailFailedException extends RuntimeException{
    public SendMailFailedException(String s){
        super(s);
    }
}
