package com.example.backend.data;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * This is a dummy for testing purposes (and proof of concept)
 */
public class NlpAnswer {

    private String what;
    private String where;
    private String spec;

    public NlpAnswer(@JsonProperty("what") String what,
                     @JsonProperty("where") String where,
                     @JsonProperty("spec") String spec) {
        this.what = what;
        this.where = where;
        this.spec = spec;
    }

    public String getWhat() {
        return what;
    }

    public String getWhere() {
        return where;
    }

    public String getSpec() {
        return spec;
    }

    @Override
    public String toString() {
        return "NlpAnswer{" +
                "what='" + what + '\'' +
                ", where='" + where + '\'' +
                ", spec='" + spec + '\'' +
                '}';
    }
}