package com.example.backend.data.http;

public class Error {
    private final String type;
    private final String message;
    private final String trace;

    private Error(String message, String trace, String type) {
        this.message = message;
        this.trace = trace;
        this.type = type;
    }

    public static Error createError(String message, String trace) {
        return new Error(message, trace, "system");
    }

    @SuppressWarnings("unused")
    public String getType() {
        return type;
    }

    @SuppressWarnings("unused")
    public String getMessage() {
        return message;
    }

    @SuppressWarnings("unused")
    public String getTrace() {
        return trace;
    }
}