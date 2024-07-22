package org.example.mobilephonebookingservice.exception;

public class PhoneNotAvailableException extends RuntimeException {
    public PhoneNotAvailableException(String message) {
        super(message);
    }
}