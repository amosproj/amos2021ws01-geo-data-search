package com.example.backend.nlp;

import com.google.gson.annotations.SerializedName;

public class Gradient {

    @SerializedName("min")
    private final Integer min;
    @SerializedName("max")
    private final Integer max;
    @SerializedName("length")
    private final Integer length;

    public Gradient(Integer min, Integer max, Integer length) {
        this.min = min;
        this.max = max;
        this.length = length;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getLength() {
        return length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Gradient gradient = (Gradient) o;
        return min.equals(gradient.min) && max.equals(gradient.max) && length.equals(gradient.length);
    }

    @Override
    public String toString() {
        return "Gradient{" +
                "\n\t\t\tmin = " + min +
                "\n\t\t\tmax = " + max +
                "\n\t\t\tlength = " + length +
                "\n\t\t\t}";
    }
}