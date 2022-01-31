package com.example.backend.controllers;

import com.example.backend.clients.NlpClient;
import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.data.http.Error;
import com.example.backend.data.http.*;
import com.example.backend.data.kml.KML;
import com.example.backend.data.kml.KMLPlaceMark;
import com.example.backend.helpers.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    public static final String BACKEND_VERSION = "0.11.0";
    private final NlpClient nlpClient;
    private final ApiController apiController;
    private final Logger logger = LogManager.getLogger("FRONTEND_CONTROLLER");

    public FrontendController(NlpClient nlpClient, OsmApiClient osmApiClient, HereApiRestService hereApiRestService) {
        this.nlpClient = nlpClient;
        this.apiController = new ApiController(osmApiClient, hereApiRestService);
    }

    /**
     * Receives the query from Frontend and forwards it to NLP
     * Receives the response from NLP and forwards it to OSM or HERE API
     * Receives the response from OSM or HERE API and logs it
     *
     * @param query the input coming from the Frontend
     * @return result the answers from the OSM or HERE API for the Frontend
     */
    @PostMapping("/user_query")
    @ResponseBody
    public HttpResponse handleQueryRequest(@RequestBody String query) {
        logger.info("+ -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- + START + -- + -- +");
        logger.info("New query received! Query = \"" + query + "\"");

        query = prepareQuery(query);

        NlpQueryResponse nlpQueryResponse;
        try {
            // Sending the query to the NLP and receiving its response here
            nlpQueryResponse = getNlpQueryResponse(query);
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            return handleError(throwable);
        }

        List<ApiResult> apiQueryResults = null;
        try {
            // The API decision and calling happens here:
            apiQueryResults = apiController.querySearch(nlpQueryResponse);
        } catch (MissingLocationException | UnknownQueryObjectException | NoPreferredApiFoundException | LocationNotFoundException | InvalidCalculationRequest e) {
            handleError(e);
        }

        ResultResponse response = prepareResponse(apiQueryResults);
        if (!logger.isDebugEnabled()) {
            logger.info("Sending this respond to FRONTEND:\n" + response.toStringWithoutPolyline());
        }

        KML kml = null;
        if (!response.getResult().isEmpty() && response.getResult().get(0) instanceof NodeInfo) {
            //OSM node info results
            kml = new KMLPlaceMark.Builder().from(response.getResult());
        } else if (!response.getResult().isEmpty()) {
            //Here Api Routing result
        }
        logger.debug("Here is the generated KML file: \n " + kml);
        logger.debug("Sending this respond to FRONTEND:\n" + response.toStringWithPolyline());
        logger.info("+ -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +  END  + -- + -- +");
        return response;
    }

    private String prepareQuery(String query) {
        query = URLDecoder.decode(query, StandardCharsets.UTF_8);
        query = query.replace("query=", "");
        logger.info("WORK AROUND! Query = \"" + query + "\"");
        return query;
    }

    private NlpQueryResponse getNlpQueryResponse(String query) {
        NlpQueryResponse nlpQueryResponse;
        logger.info("Sending data to NLP...");
        String nlpResponse = nlpClient.sendToNlp(query);
        logger.info("...SUCCESS!");
        logger.info("NLP RESPONSE:");
        logger.info(nlpResponse);
        logger.info("INTERPRETED NLP RESPONSE:");
        nlpQueryResponse = new Gson().fromJson(nlpResponse, NlpQueryResponse.class);
        logger.info(nlpQueryResponse.toString());
        return nlpQueryResponse;
    }

    // TODO Maybe we should throw an exception in the case of an empty response?
    private ResultResponse prepareResponse(List<ApiResult> apiQueryResults) {
        if (apiQueryResults == null || apiQueryResults.isEmpty()) {
            return new ResultResponse(null);
        } else {
            return new ResultResponse(apiQueryResults);
        }
    }

    private ErrorResponse handleError(Throwable throwable) {
        logger.error("...ERROR!" + "\n" + "\t" + throwable.toString());
        return new ErrorResponse(Error.createError(throwable.getMessage(), Arrays.toString(throwable.getStackTrace())));
    }

    @GetMapping("/version")
    @ResponseBody
    public HttpResponse handleVersionRequest() {
        logger.info("Version query received!");

        String nlpVersion;
        try {
            String nlpVersionResponse = nlpClient.fetchNlpVersion();
            logger.info("NLP VERSION RESPONSE:");
            logger.info(nlpVersionResponse);
            Gson g = new Gson();
            nlpVersion = g.fromJson(nlpVersionResponse, NlpVersionResponse.class).getVersion();
        } catch (Throwable t) {
            nlpVersion = "unknown";
        }

        return new VersionResponse(Version.createVersion(BACKEND_VERSION, nlpVersion));
    }
}
