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

    public static Error createError(String message) {
        return new Error(message, "NO_TRACE_AVAILABLE", "system");
    }

    public static Error createError(String message, String trace) {
        return new Error(message, trace, "system");
    }

    public static Error createError(String message, String trace, String type) {
        return new Error(message, trace, type);
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
