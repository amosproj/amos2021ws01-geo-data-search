package com.example.backend.helpers;

public class WrongFileTypeException extends Throwable {
    public WrongFileTypeException(String msg) {
        super(msg);
    }
}