package com.billing_system.auth.Exceptions;

public class UserAlreadyRegisterException extends RuntimeException {
    public UserAlreadyRegisterException() {
        super("The user is already registered. Try a different email or log in.");
    }
}

