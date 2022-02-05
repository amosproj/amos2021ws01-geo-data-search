package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import com.example.backend.helpers.Version;

public class VersionResponse implements HttpResponse {
    private final Version version;

    public VersionResponse(Version version) {
        this.version = version;
    }

    @SuppressWarnings("unused")
    public Version getResult(){
        return this.version;
    }
}