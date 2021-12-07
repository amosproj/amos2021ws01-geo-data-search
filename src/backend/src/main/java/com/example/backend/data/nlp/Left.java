package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

public class Left {

    @SerializedName("min")
    private final Integer min;
    @SerializedName("max")
    private final Integer max;
    @SerializedName("count")
    private final Integer count;

    public Left(Integer min, Integer max, Integer count) {
        this.min = min;
        this.max = max;
        this.count = count;
    }

    public Integer getMin() {
        return min;
    }

    public Integer getMax() {
        return max;
    }

    public Integer getCount() {
        return count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Left left = (Left) o;
        return min.equals(left.min) && max.equals(left.max) && count.equals(left.count);
    }

    @Override
    public String toString() {
        return "Left{" +
                "\n\t\t\t\tmin = " + min +
                "\n\t\t\t\tmax = " + max +
                "\n\t\t\t\tcount = " + count +
                "\n\t\t\t\t}";
    }
}