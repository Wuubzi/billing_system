package com.billing_system.auth.Exceptions;

public class CodeInvalidException extends RuntimeException{
    public CodeInvalidException(){
        super("Invalid reset password code. Please check the code and try again.");
    }
}
