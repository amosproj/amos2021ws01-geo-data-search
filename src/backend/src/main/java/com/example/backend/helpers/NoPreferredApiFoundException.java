package com.example.backend.helpers;

public class NoPreferredApiFoundException extends Throwable {
    public NoPreferredApiFoundException(String msg) {
        super(msg);
    }
}