package com.billing_system.auth.Exceptions;

public class PasswordSameAsPreviousException extends RuntimeException{
    public PasswordSameAsPreviousException(){
        super("The new password cannot be the same as the previous one.");
    }
}
