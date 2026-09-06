package com.gopi.airbnb.exceptions;

public class ResourceAlreadyExistsException extends  RuntimeException{

    public ResourceAlreadyExistsException (String message){
        super(message);
    }
}
