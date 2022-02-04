package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;

public class ErrorResponse implements HttpResponse {

   private final Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    @SuppressWarnings("unused")
    public Error getError(){
        return error;
    }
}