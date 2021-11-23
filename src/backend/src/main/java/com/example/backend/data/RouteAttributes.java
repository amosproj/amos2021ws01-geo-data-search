package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RouteAttributes {

    @SerializedName("height")
    private final Height height;
    @SerializedName("length")
    private final Length length;
    @SerializedName("gradiant")
    private final Gradiant gradiant;
    @SerializedName("curves")
    private final Curves curves;

    public RouteAttributes(Height height, Length length, Gradiant gradiant, Curves curves) {
        this.height = height;
        this.length = length;
        this.gradiant = gradiant;
        this.curves = curves;
    }

    public Height getHeight() {
        return height;
    }

    public Length getLength() {
        return length;
    }

    public Gradiant getGradiant() {
        return gradiant;
    }

    public Curves getCurves() {
        return curves;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteAttributes that = (RouteAttributes) o;
        return Objects.equals(height, that.height) && Objects.equals(length, that.length) && Objects.equals(gradiant, that.gradiant) && Objects.equals(curves, that.curves);
    }

    @Override
    public String toString() {
        return "RouteAttributes{" +
                "\n\t\theight = " + height +
                "\n\t\tlength = " + length +
                "\n\t\tgradiant = " + gradiant +
                "\n\t\tcurves = " + curves +
                "\n\t\t}";
    }
}