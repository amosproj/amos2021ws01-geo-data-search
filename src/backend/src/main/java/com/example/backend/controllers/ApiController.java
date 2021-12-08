package com.example.backend.controllers;

import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.api.*;
import com.example.backend.data.http.Error;
import com.example.backend.data.http.ErrorResponse;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.ApiSelectionHelper;
import com.example.backend.helpers.ApiSelectionHelper.ApiType;
import com.example.backend.helpers.BackendLogger;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/backend")
public class ApiController {

    private final OsmApiClient osmApiClient;
    private final BackendLogger logger = new BackendLogger();
    private static final String LOG_PREFIX = "API_CONTROLLER";
    private static final String dummyNodeID = "305293190";
    private final HereApiRestService hereApiRestService;

    public ApiController(OsmApiClient osmApiClient, HereApiRestService hereApiRestService) {
        this.osmApiClient = osmApiClient;
        this.hereApiRestService = hereApiRestService;
    }

    @Deprecated(since = "Requesting node info with NodeID is not working for now.")
    public NodeInfo requestNodeInfo(String nodeID) {
        logInfo("Request Info for node:" + nodeID + " from API");
        logInfo("Sending data to API...");
        //TODO replace the dummyNodeID with real one when implemented
        NodeInfo apiXMLResponse = osmApiClient.requestNode(dummyNodeID);
        logInfo("SUCCESS!, response from API received:" + apiXMLResponse);
        //TODO replace this with result when implemented
        return apiXMLResponse;
    }

    public ArrayList<ApiResult> querySearch(NlpQueryResponse nlpQueryResponse) {
        ApiType preferredApi = ApiSelectionHelper.getInstance().getApiPreference(nlpQueryResponse);
        ArrayList<ApiResult> result = new ArrayList<>();
        logInfo("Selected Api: " + preferredApi);
        if (preferredApi == ApiType.OSM_API) {
            OSMQuery query = generateOsmQuery(nlpQueryResponse);
            OSMSearchResult osmResults = osmApiClient.querySearch(query.toQuery());
            result.addAll(osmResults.getSearchResults());
        } else if (preferredApi == ApiType.HERE_API) {
            String hereApiResponseAsString = hereApiRestService.getPostsPlainJSON(generateHereQuery(nlpQueryResponse));
            HereApiGeocodeResponse hereResults = new Gson().fromJson(hereApiResponseAsString, HereApiGeocodeResponse.class);
            result.addAll(hereResults.getSearchResults());
        }
        return result;
    }

    public void routeSearch(String origin, String destination, String transportMode, String returnType) {
        try {
            String hereApiRoutingResponseString =
                    hereApiRestService.getRoutingResponse(origin, destination, transportMode, returnType);
            logInfo("HERE / ROUTING:");
            logInfo(hereApiRoutingResponseString);
            HereApiRoutingResponse hereApiRoutingResponse = new Gson().fromJson(hereApiRoutingResponseString, HereApiRoutingResponse.class);
            logInfo(hereApiRoutingResponse.toString(""));
        } catch (Throwable throwable) {
            handleError(throwable);
        }
    }

    public void guidanceSearch(String origin, String destination, String transportMode) {
        try {
            HereGuidanceResponse hereApiRoutingResponse =
                    hereApiRestService.getGuidanceResponse("52.5308,13.3847", "52.5264,13.3686", "car");
            logInfo("HERE / GUIDANCE:");
            logInfo(hereApiRoutingResponse.toString(""));
        } catch (Throwable throwable) {
            handleError(throwable);
        }
    }

    private OSMQuery generateOsmQuery(NlpQueryResponse nlpQueryResponse) {
        OSMQuery osmQuery = new OSMQuery();
//        String amenity = nlpQueryResponse.getQueryObject();
//        osmQuery.setAmenity(amenity);
        osmQuery.setAmenity("restaurant");
        //TODO we are not getting location object from NLP, yet
        osmQuery.setArea("Mitte");
        logInfo("Generated Query for OSM: " + osmQuery.toQuery());
        return osmQuery;
    }

    private String generateHereQuery(NlpQueryResponse nlpQueryResponse) {
        StringBuilder builder = new StringBuilder();
//        builder.append(nlpQueryResponse.getLocation()).append(" ");
        builder.append("Berlin ");
        builder.append(nlpQueryResponse.getQueryObject());
        logInfo("Generated Query for HERE: " + builder);
        return builder.toString();
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private ErrorResponse handleError(Throwable throwable) {
        logError("...ERROR!" + "\n" + "\t" + throwable.getMessage());
        return new ErrorResponse(Error.createError(throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}