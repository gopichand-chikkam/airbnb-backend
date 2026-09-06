package com.gopi.airbnb.exceptions;

public class ResourceNotFoundException extends ResourceAlreadyExistsException {
    public ResourceNotFoundException (String message){
        super(message);
    }
}
