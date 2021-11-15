package com.example.backend.controllers;

public class ErrorResponse implements HttpResponse {
    private Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    public Error getError(){
        return this.error;
    }
}
