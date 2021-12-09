package com.example.backend.helpers;

import com.example.backend.data.http.NlpQueryResponse;

public class ApiSelectionHelper {
    private static ApiSelectionHelper instance;
    private static final String ELEVATION = "elevation";
    private static final String PLACE = "place";
    private static final String ROUTE = "route";

    private ApiSelectionHelper() {
    }

    public static ApiSelectionHelper getInstance() {
        if (instance == null) {
            instance = new ApiSelectionHelper();
        }
        return instance;
    }

    public RequestType getRequestType(NlpQueryResponse nlpQueryResponse){
        RequestType requestType;
        switch (nlpQueryResponse.getQueryObject()) {
            case ELEVATION:
                requestType = RequestType.SEARCH;
                break;
            case PLACE:
                requestType = RequestType.DETAILS;
                break;
            case ROUTE:
                requestType = RequestType.ROUTING;
                break;
            default:
                requestType = RequestType.SEARCH;
                break;
        }
        return requestType;
    }

    public ApiType getApiPreference(NlpQueryResponse nlpQueryResponse) {
        ApiType preference;
        switch (getRequestType(nlpQueryResponse)) {
            case SEARCH:
                preference = ApiType.OSM_API;
                break;
            case ROUTING:
                preference = ApiType.HERE_API;
                break;
            case DETAILS:
                preference = ApiType.OSM_API;
                break;
            default:
                preference = ApiType.OSM_API;
                break;
        }
        return preference;
    }

    public enum ApiType {
        OSM_API, HERE_API
    }
    public enum RequestType {
        SEARCH, ROUTING, DETAILS
    }
}

