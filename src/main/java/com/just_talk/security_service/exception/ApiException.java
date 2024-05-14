package com.just_talk.security_service.exception;

public class ApiException extends RuntimeException{
    protected String errorCode;

    public ApiException(String message, String errorCode){
        super(message);
        this.errorCode=errorCode;
    }
}
