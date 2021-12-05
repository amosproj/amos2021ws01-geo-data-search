package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Item {

    @SerializedName("title")
    public String title;
    @SerializedName("id")
    public String id;
    @SerializedName("resultType")
    public String resultType;
    @SerializedName("address")
    public Address address;
    @SerializedName("position")
    public Position position;
    @SerializedName("mapView")
    public MapView mapView;
    @SerializedName("scoring")
    public Object scoring;

    public Item(String title, String id, String resultType, Address address, Position position, MapView mapView, Object scoring) {
        this.title = title;
        this.id = id;
        this.resultType = resultType;
        this.address = address;
        this.position = position;
        this.mapView = mapView;
        this.scoring = scoring;
    }

    public String toString(String tab) {
        return "\n" + tab + "Item{" +
                "\n" + tab + "\ttitle = \"" + title + "\"" +
                "\n" + tab + "\tid = \"" + id + "\"" +
                "\n" + tab + "\tresultType = " + resultType + "\"" +
                "\n" + tab + "\taddress = " + address.toString(tab + "\t\t") +
                "\n" + tab + "\tposition = " + position.toString(tab + "\t\t") +
                "\n" + tab + "\tmapView = " + mapView.toString(tab + "\t\t") +
                "\n" + tab + "\tscoring = " + scoring.toString() +
                "\n" + tab + "\t}";
    }
}