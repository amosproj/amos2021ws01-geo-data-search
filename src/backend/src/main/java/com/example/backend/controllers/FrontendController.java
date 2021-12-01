package com.example.backend.controllers;

import com.example.backend.clients.ApiClient;
import com.example.backend.clients.NlpClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.api.HereApiGeocodeResponse;
import com.example.backend.data.HttpResponse;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.data.api.OSMQuery;
import com.example.backend.data.api.OSMSearchResult;
import com.example.backend.data.http.Error;
import com.example.backend.data.http.*;
import com.example.backend.helpers.BackendLogger;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    private final NlpClient nlpClient;
    private final BackendLogger logger = new BackendLogger();
    private final ApiController apiController;
    private static final String LOG_PREFIX = "FRONTEND_CONTROLLER";
    private final HereApiRestService rs;

    public FrontendController(NlpClient nlpClient, ApiClient apiClient, HereApiRestService rs) {
        this.nlpClient = nlpClient;
        this.apiController = new ApiController(apiClient);
        this.rs = rs;
    }

    /**
     * Receives the query from Frontend and forwards it to NLP
     * Receives the response from NLP and forwards it to OSM/HERE API
     * Receives the response from OSM/HERE API and logs it
     *
     * @param query the input coming from the Frontend
     */
    @PostMapping("/user_query")
    @ResponseBody
    public HttpResponse handleQueryRequest(@RequestBody String query) {
        logInfo("New query received! Query = " + query);

        try {
            logInfo("Sending data to NLP...");
            String nlpResponse = nlpClient.sendToNlp(query);
            logInfo("...SUCCESS!, response from NLP received:" + nlpResponse);
            logInfo("NLP RESPONSE:");
            logInfo(nlpResponse);
            logInfo("INTERPRETED NLP RESPONSE:");
            logInfo(new Gson().fromJson(nlpResponse, NlpQueryResponse.class).toString());
        } catch (Throwable throwable) {
            return handleError(throwable);
        }

        OSMQuery osmQuery = createDummySearchQuery();
        logInfo("OSM Search Query: ");
        logInfo(osmQuery.toQuery());

        OSMSearchResult osmResults = apiController.querySearch(osmQuery.toQuery());
        logInfo("Search results: ");
        logInfo(osmResults.toString());

        HereApiGeocodeResponse hereApiGeocodeResponse;
        try {
            hereApiGeocodeResponse = getApiGeocodeResponse("autobahn+berlin+innsbrucker+platz");
        } catch (Throwable throwable) {
            return handleError(throwable);
        }

        logInfo("INTERPRETED API RESPONSE:");
        logInfo("OSM:");
        logInfo(osmResults.toString());
        logInfo("HERE:");
        logInfo(hereApiGeocodeResponse.toString());

        ArrayList<ApiResult> results = new ArrayList<>();
        results.addAll(osmResults.getSearchResults());

        return new ResultResponse(results);
    }

    private OSMQuery createDummySearchQuery() {
        OSMQuery osmQuery = new OSMQuery();
        osmQuery.setAmenity("restaurant");
        osmQuery.setArea("Mitte");
        return osmQuery;
    }

    private ErrorResponse handleError(Throwable throwable) {
        logError("...ERROR!" + "\n" + "\t" + throwable.getMessage());
        return new ErrorResponse(Error.createError(throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    private HereApiGeocodeResponse getApiGeocodeResponse(String query) {
        String hereApiResponseAsString = rs.getPostsPlainJSON(query);
        return new Gson().fromJson(hereApiResponseAsString, HereApiGeocodeResponse.class);
    }

    private NodeInfo getOsmApiResponse(String nodeId) {
        return this.apiController.requestNodeInfo(nodeId);
    }

    @GetMapping("/version")
    @ResponseBody
    public HttpResponse handleVersionRequest() {
        logInfo("Version query received!");

        String nlpVersion;
        try {
            String nlpVersionResponse = nlpClient.fetchNlpVersion();
            logInfo("NLP VERSION RESPONSE:");
            logInfo(nlpVersionResponse);
            Gson g = new Gson();
            nlpVersion = g.fromJson(nlpVersionResponse, NlpVersionResponse.class).getVersion();
        } catch (Throwable t) {
            nlpVersion = "unknown";
        }

        return new VersionResponse(Version.createVersion("0.5.0", nlpVersion));
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}