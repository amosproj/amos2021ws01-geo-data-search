package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Curves {

    @SerializedName("min")
    private final Integer min;
    @SerializedName("max")
    private final Integer max;
    @SerializedName("count")
    private final Integer count;
    @SerializedName("left")
    private final Left left;
    @SerializedName("right")
    private final Right right;

    public Curves(Integer min, Integer max, Integer count, Left left, Right right) {
        this.min = min;
        this.max = max;
        this.count = count;
        this.left = left;
        this.right = right;
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

    public Left getLeft() {
        return left;
    }

    public Right getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Curves curves = (Curves) o;
        return min.equals(curves.min) && max.equals(curves.max) && count.equals(curves.count) && Objects.equals(left, curves.left) && Objects.equals(right, curves.right);
    }

    @Override
    public String toString() {
        return "Curves{" +
                "\n\t\t\tmin = " + min +
                "\n\t\t\tmax = " + max +
                "\n\t\t\tcount = " + count +
                "\n\t\t\tleft = " + left +
                "\n\t\t\tright = " + right +
                "\n\t\t\t}";
    }
}