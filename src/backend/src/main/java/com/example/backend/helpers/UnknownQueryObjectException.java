package com.example.backend.helpers;

public class UnknownQueryObjectException extends Throwable {
    public UnknownQueryObjectException(String msg) {
        super(msg);
    }
}