package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class Curves {

    @SerializedName("min")
    private final String min;
    @SerializedName("max")
    private final String max;
    @SerializedName("count")
    private final String count;
    @SerializedName("left")
    private final Left left;
    @SerializedName("right")
    private final Right right;

    public Curves(String min, String max, String count, Left left, Right right) {
        this.min = min;
        this.max = max;
        this.count = count;
        this.left = left;
        this.right = right;
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
        return min == curves.min && max == curves.max && count == curves.count && Objects.equals(left, curves.left) && Objects.equals(right, curves.right);
    }

    @Override
    public String toString() {
        return "Curves{" +
                "\n\t\t\tmin = \"" + min + "\"" +
                "\n\t\t\tmax = \"" + max + "\"" +
                "\n\t\t\tcount = \"" + count + "\"" +
                "\n\t\t\tleft = " + left +
                "\n\t\t\tright = " + right +
                "\n\t\t\t}";
    }
}