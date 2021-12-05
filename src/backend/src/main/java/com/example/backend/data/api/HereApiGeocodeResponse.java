package com.example.backend.data.api;

import com.example.backend.data.here.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HereApiGeocodeResponse {

    @SerializedName("items")
    public List<Item> items;

    public HereApiGeocodeResponse(List<Item> items) {
        this.items = items;
    }

    public String toString(String tab) {
        return "\n" + tab + "HereApiGeocodeResponse{" +
                "\n" + tab + "\titems = " + items.get(0).toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}