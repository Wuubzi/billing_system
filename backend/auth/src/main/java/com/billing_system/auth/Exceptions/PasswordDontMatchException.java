package com.billing_system.auth.Exceptions;

public class PasswordDontMatchException extends RuntimeException{
    public PasswordDontMatchException(){
        super("The password you entered is incorrect. Please check and try again.");
    }
}
