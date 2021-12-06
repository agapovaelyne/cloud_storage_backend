package com.example.CloudKeeper.exception;

public class UnauthorizedException extends CloudException {
    public UnauthorizedException(String msg) {
        super("Unauthorized: " + msg);
    }
}