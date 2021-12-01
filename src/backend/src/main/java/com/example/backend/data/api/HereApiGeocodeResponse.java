package com.example.backend.data.api;

import com.example.backend.data.ApiResult;
import com.example.backend.data.here.Item;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HereApiGeocodeResponse {

    @SerializedName("items")
    List<Item> items;

    public HereApiGeocodeResponse() {
    }

    public HereApiGeocodeResponse(List<Item> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "\nHereApiResponse{" +
                "\n\titems = \"" + items + "\"" +
                "\n\t}";
    }
}