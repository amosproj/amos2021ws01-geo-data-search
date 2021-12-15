package com.example.backend.controllers;

import com.example.backend.clients.NlpClient;
import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
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

    // Brandenburger Tor in Berlin
    public static final String ROUTE_START_COORDINATES = "52.518462144205756,13.373228882261595";
    // Arc de Triomphe de l’Étoile in Paris
    public static final String ROUTE_DESTINATION_COORDINATES = "48.873970150314705,2.2949678134907785";

    public FrontendController(NlpClient nlpClient, OsmApiClient osmApiClient, HereApiRestService hereApiRestService) {
        this.nlpClient = nlpClient;
        this.apiController = new ApiController(osmApiClient, hereApiRestService);
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
        logInfo("New query received! Query = \"" + query + "\"");

        query = query.replace('+', ' ');
        query = query.replace("query=", "");
        logInfo("WORK AROUND! Query = \"" + query + "\"");
        NlpQueryResponse nlpQueryResponse;
        try {
            logInfo("Sending data to NLP...");
            String nlpResponse = nlpClient.sendToNlp(query);
            logInfo("...SUCCESS!, response from NLP received:" + nlpResponse);
            logInfo("NLP RESPONSE:");
            logInfo(nlpResponse);
            logInfo("INTERPRETED NLP RESPONSE:");
            nlpQueryResponse = new Gson().fromJson(nlpResponse, NlpQueryResponse.class);
            logInfo(nlpQueryResponse.toString());
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return handleError(throwable);
        }

        ArrayList<ApiResult> apiQueryResults = apiController.querySearch(nlpQueryResponse);

        apiQueryResults.addAll(apiController.getChargingStationsAlongTheWay(ROUTE_START_COORDINATES, ROUTE_DESTINATION_COORDINATES));

        return new ResultResponse(apiQueryResults);
    }

    private ErrorResponse handleError(Throwable throwable) {
        logError("...ERROR!" + "\n" + "\t" + throwable.getMessage());
        return new ErrorResponse(Error.createError(throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
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