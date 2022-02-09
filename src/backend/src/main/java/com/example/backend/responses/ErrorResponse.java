package com.example.backend.responses;

import com.example.backend.helpers.Error;

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