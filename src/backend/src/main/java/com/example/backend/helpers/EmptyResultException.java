package com.example.backend.helpers;

public class EmptyResultException extends Throwable {
    public EmptyResultException(String msg) {
        super(msg);
    }
}