package com.alchotest.spring.jwt.mongodb.exception;

public class SubjNotFoundException extends RuntimeException {
    public SubjNotFoundException(String message) {
        super(message);
    }
}
