package com.billing_system.auth.Exceptions;

public class BadVerifyCodeException extends RuntimeException{
    public BadVerifyCodeException(){
        super("The verification code provided is invalid. Please check and try again.");
    }
}
