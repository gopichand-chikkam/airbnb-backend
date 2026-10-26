package com.gopi.airbnb.exceptions;

public class BusinessException extends RuntimeException{

    public BusinessException (String message){
        super(message);
    }
}
