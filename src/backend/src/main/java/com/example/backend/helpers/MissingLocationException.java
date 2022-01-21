package com.example.backend.helpers;

public class MissingLocationException extends Throwable {
    public MissingLocationException(String msg) {
        super(msg);
    }
}