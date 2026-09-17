package com.prtms.exception;

public class PlatformNotFoundException extends RuntimeException {
    public PlatformNotFoundException(String platformCode) {
        super("Platform not found: " + platformCode);
    }
}
