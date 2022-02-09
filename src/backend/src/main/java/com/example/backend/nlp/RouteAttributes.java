package com.example.backend.nlp;

import com.google.gson.annotations.SerializedName;

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
    @SerializedName("toll_road_avoidance")
    private final Boolean toll_road_avoidance;
    @SerializedName("location_start")
    private final String routeOrigin;
    @SerializedName("location_end")
    private final String routeDestination;

    public RouteAttributes(Height height, Length length, Gradient gradient, Curves curves, Boolean charging_stations, Boolean toll_road_avoidance, String routeOrigin, String routeDestination) {
        this.height = height;
        this.length = length;
        this.gradient = gradient;
        this.curves = curves;
        this.chargingStations = charging_stations;
        this.toll_road_avoidance = toll_road_avoidance;
        this.routeOrigin = routeOrigin;
        this.routeDestination = routeDestination;
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

    public Boolean shouldTollRoutesBeAvoided() {
        return toll_road_avoidance;
    }

    public String getRouteOrigin() {
        return routeOrigin;
    }

    public String getRouteDestination() {
        return routeDestination;
    }

    @Override
    public String toString() {
        return "RouteAttributes{" +
                "\n\t\theight = " + height +
                "\n\t\tlength = " + length +
                "\n\t\tgradient = " + gradient +
                "\n\t\tcurves = " + curves +
                "\n\t\tcharging_stations = " + chargingStations +
                "\n\t\ttoll_road_avoidance = " + toll_road_avoidance +
                "\n\t\trouteOrigin = " + routeOrigin +
                "\n\t\trouteDestination = " + routeDestination +
                "\n\t\t}";
    }
}