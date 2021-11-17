package com.example.backend.data;

import com.google.gson.annotations.SerializedName;

/**
 * This is a dummy for testing purposes (and proof of concept)
 */
public class NlpResponse {

    private final String location;
    @SerializedName("max distance")
    private final String maxDistance;
    @SerializedName("query object")
    private final String queryObject;
//    @SerializedName("route attributes")
//    private final String routeAttributes;

    public NlpResponse(
            String location,
            String maxDistance,
            String queryObject
//            String routeAttributes
    ) {
        this.location = location;
        this.maxDistance = maxDistance;
        this.queryObject = queryObject;
//        this.routeAttributes = routeAttributes;
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

//    public String getRouteAttributes() {
//        return routeAttributes;
//    }

    @Override
    public String toString() {
        return "NlpResponse{" +
                "location='" + location + '\'' +
                ", maxDistance='" + maxDistance + '\'' +
                ", queryObject='" + queryObject + '\'' +
                '}';
    }
}
