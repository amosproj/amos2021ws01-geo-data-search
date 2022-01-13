package com.example.backend.data.here;

// TODO Analyze if we really need this extra structure. Maybe replace with SingleLocationResult?
public class RoutingWaypoint {

    private final String name;
    private double lat;
    private double lng;

    public RoutingWaypoint(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void updateCoordinates(double lat, double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public String getCoordinatesAsString() {
        return lat + "," + lng;
    }
}