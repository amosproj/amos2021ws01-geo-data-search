package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class Result implements HttpResponse {
    //TODO implement this
    private Result() {
    }

    public static Result createResult() {
        return new Result();
    }
}
