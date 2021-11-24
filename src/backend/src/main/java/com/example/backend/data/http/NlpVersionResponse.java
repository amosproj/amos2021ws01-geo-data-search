package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import com.example.backend.data.nlp.RouteAttributes;
import com.google.gson.annotations.SerializedName;

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
