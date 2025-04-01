package com.billing_system.auth.Exceptions;

public class UserNotExistException extends RuntimeException{
    public UserNotExistException(){
        super("The user does not exist. Please check your credentials");
    }
}
