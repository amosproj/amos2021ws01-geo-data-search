package com.example.backend.helpers;

public class LocationNotFoundException extends Throwable {
    public LocationNotFoundException(String msg) {
        super(msg);
    }
}
