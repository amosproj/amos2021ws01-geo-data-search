package com.example.backend.data.kml;

public class PlaceMark {
    private String name;
    private String description;
    private String lat;
    private String lon;

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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getCoordinates() {
        return String.format("%s, %s, 0", getLon(), getLat());
    }
}
