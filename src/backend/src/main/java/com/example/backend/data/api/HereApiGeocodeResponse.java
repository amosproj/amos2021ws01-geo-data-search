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
                "\n" + tab + "\titems = " + createListAsString(items, tab + "\t\t") +
                "\n" + tab + "\t}";
    }

    public List<Item> getSearchResults() {
        return items;
    }

    private String createListAsString(List<Item> sections, String tab) {
        if (sections != null && !sections.isEmpty()) {
            StringBuilder returnString = new StringBuilder();
            for (Item section : sections) {
                returnString.append(section.toString(tab));
                returnString.append("\n");
            }
            return returnString.toString();
        } else {
            return "null";
        }
    }
}