package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

public class Length {

    @SerializedName("min")
    private final String min;
    @SerializedName("max")
    private final String max;

    public Length(String min, String max) {
        this.min = min;
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Length length = (Length) o;
        return min == length.min && max == length.max;
    }

    @Override
    public String toString() {
        return "Length{" +
                "\n\t\t\tmin = \"" + min + "\"" +
                "\n\t\t\tmax = \"" + max + "\"" +
                "\n\t\t\t}";
    }
}