package com.example.backend.nlp;

import com.google.gson.annotations.SerializedName;

public class Height {

    @SerializedName("min")
    private final Integer min;
    @SerializedName("max")
    private final Integer max;

    public Height(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Height height = (Height) o;
        return min.equals(height.min) && max.equals(height.max);
    }

    @Override
    public String toString() {
        return "Height{" +
                "\n\t\t\tmin = " + min +
                "\n\t\t\tmax = " + max +
                "\n\t\t\t}";
    }
}