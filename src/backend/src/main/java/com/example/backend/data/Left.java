package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

public class Left {

    @SerializedName("min")
    private final String min;
    @SerializedName("max")
    private final String max;
    @SerializedName("count")
    private final String count;

    public Left(String min, String max, String count) {
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public String getMin() {
        return min;
    }

    public String getMax() {
        return max;
    }

    public String getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Left left = (Left) o;
        return min == left.min && max == left.max && count == left.count;
    }

    @Override
    public String toString() {
        return "Left{" +
                "\n\t\t\t\tmin = \"" + min + "\"" +
                "\n\t\t\t\tmax = \"" + max + "\"" +
                "\n\t\t\t\tcount = \"" + count + "\"" +
                "\n\t\t\t\t}";
    }
}