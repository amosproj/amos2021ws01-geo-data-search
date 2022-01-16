package com.example.backend.helpers;

public class NoPrefferedApiFoundException extends Throwable {
    public NoPrefferedApiFoundException(String msg) {
        super(msg);
    }
}