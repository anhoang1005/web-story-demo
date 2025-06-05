package com.example.webstorydemo.exceptions.external;

public class FileFailedUploadException extends RuntimeException{
    public FileFailedUploadException(String message){
        super(message);
    }
}
