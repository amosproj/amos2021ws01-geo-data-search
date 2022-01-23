package com.example.backend.data.here;

public class RoutingWaypoint {

    private String name;
    private double lat;
    private double lng;

    public RoutingWaypoint(String name) {
        this.name = name.trim();
    }

    public RoutingWaypoint(String name, double lat, double lng) {
        this.name = name.trim();
        this.lat = lat;
        this.lng = lng;
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

    public double getLatitude() {
        return lat;
    }

    public double getLongitude() {
        return lng;
    }

    public void setName(String name) {
        this.name = name;
    }
}