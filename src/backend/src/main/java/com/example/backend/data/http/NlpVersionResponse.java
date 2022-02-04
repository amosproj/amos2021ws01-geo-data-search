package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;

/**
 * The data structure to hold the response from the NLP component.
 */
public class NlpVersionResponse implements HttpResponse {

    private final String version;

    public NlpVersionResponse(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
