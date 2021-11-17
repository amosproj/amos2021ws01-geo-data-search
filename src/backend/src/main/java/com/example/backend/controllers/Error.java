package com.example.backend.controllers;

public class Error {
    private final String type = "system";
    private final String message;
    private final String trace = "HERE COME TRACE";

    private Error(String message) {
        this.message = message;
    }

    public static Error createError(String message) {
        return new Error(message);
    }

    public String getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public String getTrace() {
        return trace;
    }
}
