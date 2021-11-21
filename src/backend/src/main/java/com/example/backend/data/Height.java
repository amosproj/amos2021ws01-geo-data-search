package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

public class Height {

    @SerializedName("min")
    private final String min;
    @SerializedName("max")
    private final String max;

    public Height(String min, String max) {
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
        Height height = (Height) o;
        return min == height.min && max == height.max;
    }

    @Override
    public String toString() {
        return "Height{" +
                "\n\t\t\tmin = \"" + min + "\"" +
                "\n\t\t\tmax = \"" + max + "\"" +
                "\n\t\t\t}";
    }
}