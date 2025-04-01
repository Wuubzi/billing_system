package com.billing_system.auth.Exceptions;

public class UserNotExistsException extends RuntimeException {
    public UserNotExistsException(){
        super("The user does not exist. Please check the provided information.");
    }
}
