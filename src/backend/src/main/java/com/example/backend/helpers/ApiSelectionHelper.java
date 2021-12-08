package com.example.backend.helpers;

import com.example.backend.data.http.NlpQueryResponse;

public class ApiSelectionHelper {
    private static ApiSelectionHelper instance;

    private ApiSelectionHelper() {
    }

    public static ApiSelectionHelper getInstance() {
        if (instance == null) {
            instance = new ApiSelectionHelper();
        }
        return instance;
    }

    public ApiType getApiPreference(NlpQueryResponse nlpQueryResponse) {
        return ApiType.OSM_API;
    }

    public enum ApiType {
        OSM_API, HERE_API
    }
}

