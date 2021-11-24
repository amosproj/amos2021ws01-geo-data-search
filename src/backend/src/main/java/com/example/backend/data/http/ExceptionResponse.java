package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;

public class ExceptionResponse implements HttpResponse {
    private Exception exception;

    public ExceptionResponse(Exception exception) {
        this.exception = exception;
    }

    public Exception getException() {
        return exception;
    }
}
