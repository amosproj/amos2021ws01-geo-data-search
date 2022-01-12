package com.example.backend.data.nlp;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class RouteAttributes {

    @SerializedName("height")
    private final Height height;
    @SerializedName("length")
    private final Length length;
    @SerializedName("gradiant")
    private final Gradient gradient;
    @SerializedName("curves")
    private final Curves curves;
    @SerializedName("charging_stations")
    private final Boolean chargingStations;
    @SerializedName("toll_roads")
    private final Boolean tolLRoads;

    public RouteAttributes(Height height, Length length, Gradient gradient, Curves curves, Boolean charging_stations, Boolean tolLRoads) {
        this.height = height;
        this.length = length;
        this.gradient = gradient;
        this.curves = curves;
        this.chargingStations = charging_stations;
        this.tolLRoads = tolLRoads;
    }

    public Height getHeight() {
        return height;
    }

    public Length getLength() {
        return length;
    }

    public Gradient getGradient() {
        return gradient;
    }

    public Curves getCurves() {
        return curves;
    }

    public Boolean getChargingStations() {
        return chargingStations;
    }

    public Boolean getTollRoads() {
        return tolLRoads;
    }

    @Override
    public String toString() {
        return "RouteAttributes{" +
                "\n\t\theight = " + height +
                "\n\t\tlength = " + length +
                "\n\t\tgradient = " + gradient +
                "\n\t\tcurves = " + curves +
                "\n\t\tchargingStations = " + chargingStations +
                "\n\t\ttolLRoads = " + tolLRoads +
                "\n\t\t}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RouteAttributes that = (RouteAttributes) o;
        return Objects.equals(height, that.height) && Objects.equals(length, that.length) && Objects.equals(gradient, that.gradient) && Objects.equals(curves, that.curves) && Objects.equals(chargingStations, that.chargingStations) && Objects.equals(tolLRoads, that.tolLRoads);
    }
}