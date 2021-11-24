package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import com.example.backend.data.nlp.RouteAttributes;
import com.google.gson.annotations.SerializedName;

/**
 * The data structure to hold the response from the NLP component.
 */
public class NlpQueryResponse implements HttpResponse {

    @SerializedName("location")
    private final String location;
    @SerializedName("max_distance")
    private final String maxDistance;
    @SerializedName("query_object")
    private final String queryObject;
    @SerializedName("route_attributes")
    private final RouteAttributes routeAttributes;

    public NlpQueryResponse(String location, String maxDistance, String queryObject, RouteAttributes routeAttributes) {
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
        return "\nNlpResponse{" +
                "\n\tlocation = \"" + location + "\"" +
                "\n\tmaxDistance = \"" + maxDistance + "\"" +
                "\n\tqueryObject = \"" + queryObject + "\"" +
                "\n\trouteAttributes = " + routeAttributes +
                "\n\t}";
    }
}
