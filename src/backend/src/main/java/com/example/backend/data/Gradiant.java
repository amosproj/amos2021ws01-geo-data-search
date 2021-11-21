package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

public class Gradiant {

    @SerializedName("min")
    private final String min;
    @SerializedName("max")
    private final String max;
    @SerializedName("length")
    private final String length;

    public Gradiant(String min, String max, String length) {
        this.min = min;
        this.max = max;
        this.length = length;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gradiant gradiant = (Gradiant) o;
        return min == gradiant.min && max == gradiant.max && length == gradiant.length;
    }

    @Override
    public String toString() {
        return "Gradiant{" +
                "\n\t\t\tmin = \"" + min + "\"" +
                "\n\t\t\tmax = \"" + max + "\"" +
                "\n\t\t\tlength = \"" + length + "\"" +
                "\n\t\t\t}";
    }
}