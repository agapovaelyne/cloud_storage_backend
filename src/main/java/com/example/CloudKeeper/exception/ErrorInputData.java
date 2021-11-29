package com.example.CloudKeeper.exception;

public class ErrorInputData extends CloudException {
    public ErrorInputData(String msg) {
        super("Error input data: " + msg);
    }
}
