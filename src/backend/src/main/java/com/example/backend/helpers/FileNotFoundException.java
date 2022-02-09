package com.example.backend.helpers;

public class FileNotFoundException extends Throwable {
    public FileNotFoundException(String msg) {
        super(msg);
    }
}