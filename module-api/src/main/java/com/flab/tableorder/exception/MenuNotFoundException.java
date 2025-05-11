package com.flab.tableorder.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(String storeId) {
        super("Menu not found for StoreId: " + storeId);
    }
}
