package com.example.backend.helpers;

public class InvalidCalculationRequest extends Exception {
    public InvalidCalculationRequest(String msg) {
        super(msg);
    }
}