package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;

public class ErrorResponse implements HttpResponse {
    private Error error;

    public ErrorResponse(Error error) {
        this.error = error;
    }

    public Error getError(){
        return this.error;
    }
}
