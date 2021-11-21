package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

/**
 * The data structure to hold the response from the NLP component.
 */
public class NlpResponse {

    @SerializedName("location")
    private final String location;
    @SerializedName("max_distance")
    private final String maxDistance;
    @SerializedName("query_object")
    private final String queryObject;
    @SerializedName("route_attributes")
    private final RouteAttributes routeAttributes;

    public NlpResponse(String location, String maxDistance, String queryObject, RouteAttributes routeAttributes) {
        this.location = location;
        this.maxDistance = maxDistance;
        this.queryObject = queryObject;
        this.routeAttributes = routeAttributes;
    }

    public String getLocation() {
        return location;
    }

    public String getMaxDistance() {
        return maxDistance;
    }

    public String getQueryObject() {
        return queryObject;
    }

    public RouteAttributes getRouteAttributes() {
        return routeAttributes;
    }

    @Override
    public String toString() {
        return "NlpResponse{" +
                "location='" + location + '\'' +
                ", max_distance=" + maxDistance +
                ", query_object='" + queryObject + '\'' +
                ", route_attributes=" + routeAttributes +
                '}';
    }
}