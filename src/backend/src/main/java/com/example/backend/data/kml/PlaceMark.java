package com.example.backend.data.kml;

public class PlaceMark {
    private String name;
    private final String description;
    private final String lat;
    private final String lon;

    public PlaceMark(String name, String description, String lat, String lon) {
        this.name = name;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getCoordinates() {
        return String.format("%s, %s, 0", getLon(), getLat());
    }
}