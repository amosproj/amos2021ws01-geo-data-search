package com.example.backend.data;

/**
 * This is a dummy for testing purposes (and proof of concept)
 */
public class NlpAnswer {

    private final String what;
    private final String where;
    private final String spec;

    public NlpAnswer(String what, String where, String spec) {
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