package com.example.backend.responses;

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