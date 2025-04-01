package com.billing_system.auth.Exceptions;

public class PhoneNumberAlreadyExistException extends RuntimeException {
    public PhoneNumberAlreadyExistException() {
        super("The phone number is already registered. Please use a different number.");
    }
}

