package com.alchotest.spring.jwt.mongodb.exception;

public class BadRoleException extends RuntimeException {
    public BadRoleException(String message) {
        super(message);
    }
}
