package com.flab.tableorder.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(String apiKey) {
        super("Store not found for API key: " + apiKey);
    }
}