package com.avengers.gamera.exception;

public class EmailAddressException extends RuntimeException {
    public EmailAddressException() {
        super("The Email address is not valid.");
    }
}