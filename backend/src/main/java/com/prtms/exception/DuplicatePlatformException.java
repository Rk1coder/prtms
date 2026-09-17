package com.prtms.exception;

public class DuplicatePlatformException extends RuntimeException {
    public DuplicatePlatformException(String platformCode) {
        super("Platform already exists: " + platformCode);
    }
}
