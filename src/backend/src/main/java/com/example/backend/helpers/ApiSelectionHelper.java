package com.example.backend.helpers;

import com.example.backend.responses.NlpQueryResponse;

public class ApiSelectionHelper {

    private ApiSelectionHelper() {
        // empty constructor
    }

    /**
     * Decides which API should be used based on query_object in NlpQueryResponse.
     *
     * @param nlpQueryResponse the answer from the NLP
     * @return ApiType to be used for this query_object
     * @throws UnknownQueryObjectException when the query_object is unknown
     */
    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static ApiType getApiPreference(NlpQueryResponse nlpQueryResponse) throws UnknownQueryObjectException {
        String queryObject = nlpQueryResponse.getQueryObject();
        switch (queryObject) {
            case NlpQueryResponse.QUERY_OBJECT_ROUTE:
                return ApiType.HERE_API;
            case NlpQueryResponse.QUERY_OBJECT_ELEVATION:
                return ApiType.OSM_API;
            case NlpQueryResponse.QUERY_OBJECT_PLACE:
                return ApiType.OSM_API;
            default:
                throw new UnknownQueryObjectException("Query object \"" + queryObject + "\" is unknown! No decision possible, on which API to use!");
        }
    }

    public enum ApiType {
        OSM_API, HERE_API
    }
}