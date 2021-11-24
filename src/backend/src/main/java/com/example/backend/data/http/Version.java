package com.example.backend.data.http;

public class Version {
    private final String backend;
    private final String nlp;

    private Version(String backend, String nlp) {
        this.backend = backend;
        this.nlp = nlp;
    }

    public static Version createVersion(String backend, String nlp) {
        return new Version(backend, nlp);
    }

    public String getBackend() {
        return backend;
    }

    public String getNlp() {
        return nlp;
    }
}
