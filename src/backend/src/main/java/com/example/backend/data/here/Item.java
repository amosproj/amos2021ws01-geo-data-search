package com.example.backend.data.here;

import com.example.backend.data.ApiResult;
import com.google.gson.annotations.SerializedName;

public class Item implements ApiResult {

    @SerializedName("title")
    String title;
    @SerializedName("id")
    String id;
    @SerializedName("resultType")
    String resultType;
    @SerializedName("address")
    Address address;
    @SerializedName("position")
    Position position;
    @SerializedName("mapView")
    MapView mapView;
    @SerializedName("scoring")
    Object scoring;

    public Item() {

    }

    public Item(String title, String id, String resultType, Address address, Position position, MapView mapView, Object scoring) {
        this.title = title;
        this.id = id;
        this.resultType = resultType;
        this.address = address;
        this.position = position;
        this.mapView = mapView;
        this.scoring = scoring;
    }

    @Override
    public String toString() {
        return "\n\t\tItem{" +
                "\n\t\t\ttitle = \"" + title + "\"" +
                "\n\t\t\tid = \"" + id + "\"" +
                "\n\t\t\tresultType = \"" + resultType + "\"" +
                "\n\t\t\taddress = \"" + address + "\"" +
                "\n\t\t\tposition = \"" + position + "\"" +
                "\n\t\t\tmapView = = \"" + mapView + "\"" +
                "\n\t\t\tscoring = \"" + scoring + "\"" +
                "\n\t\t\t}";
    }

    @Override
    public String getType() {
        return resultType;
    }

    @Override
    public int getId() {
        return 0;
    }

    @Override
    public String getLat() {
        return String.valueOf(position.lat);
    }

    @Override
    public String getLon() {
        return String.valueOf(position.lng);
    }

    @Override
    public String getName() {
        return title;
    }
}