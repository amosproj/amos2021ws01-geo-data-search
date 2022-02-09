package com.example.backend.api.here.data;

import com.example.backend.api.here.HereApiElement;
import com.google.gson.annotations.SerializedName;

public class Item implements HereApiElement {

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
                "\n" + tab + "\taddress = " + print(address, tab) +
                "\n" + tab + "\tposition = " + print(position, tab) +
                "\n" + tab + "\tmapView = " + print(mapView, tab) +
                "\n" + tab + "\tscoring = " + print(scoring) +
                "\n" + tab + "\t}";
    }

    private String print(Object obj) {
        if (obj != null) {
            return obj.toString();
        } else {
            return "null";
        }
    }

    private String print(HereApiElement element, String tab) {
        if (element != null) {
            return element.toString(tab + "\t\t");
        } else {
            return "null";
        }
    }
}