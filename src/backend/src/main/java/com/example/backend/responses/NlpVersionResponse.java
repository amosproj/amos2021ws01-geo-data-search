package com.example.backend.responses;

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
