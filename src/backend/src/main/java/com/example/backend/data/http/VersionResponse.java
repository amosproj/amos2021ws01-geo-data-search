package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;

public class VersionResponse implements HttpResponse {
    private Version version;

    public VersionResponse(Version version) {
        this.version = version;
    }

    public Version getResult(){
        return this.version;
    }
}
