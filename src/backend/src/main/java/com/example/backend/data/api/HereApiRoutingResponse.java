package com.example.backend.data.api;

import com.example.backend.data.here.Route;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class HereApiRoutingResponse {

    @SerializedName("routes")
    public List<Route> routes;

    public HereApiRoutingResponse(List<Route> routes) {
        this.routes = routes;
    }

    public String toString(String tab) {
        return "\n" + tab + "HereApiRoutingResponse{" +
                "\n" + tab + "\troutes = " + routes.get(0).toString(tab + "\t\t") +
                "\n" + tab + "\t}";
    }
}