package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

public class Gradiant {

    @SerializedName("min")
    private final Integer min;
    @SerializedName("max")
    private final Integer max;
    @SerializedName("length")
    private final Integer length;

    public Gradiant(Integer min, Integer max, Integer length) {
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
        Gradiant gradiant = (Gradiant) o;
        return min.equals(gradiant.min) && max.equals(gradiant.max) && length.equals(gradiant.length);
    }

    @Override
    public String toString() {
        return "Gradiant{" +
                "\n\t\t\tmin = " + min +
                "\n\t\t\tmax = " + max +
                "\n\t\t\tlength = " + length +
                "\n\t\t\t}";
    }
}