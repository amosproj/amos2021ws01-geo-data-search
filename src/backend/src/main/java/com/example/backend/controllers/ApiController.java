package com.example.backend.controllers;

import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.data.api.OSMQuery;
import com.example.backend.data.api.OSMSearchResult;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.*;
import com.example.backend.helpers.ApiSelectionHelper.ApiType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * This method will make the decision, which API will be used and then calls this API
     *
     * @param nlpQueryResponse the answer from NLP containing information from the query
     * @return the results from the called API's
     * @throws MissingLocationException if Routing is selected and the location is empty, this Exception will be thrown
     */
    public List<ApiResult> querySearch(NlpQueryResponse nlpQueryResponse) throws MissingLocationException, UnknownQueryObjectException, NoPrefferedApiFoundException {
        ApiType preferredApi = ApiSelectionHelper.getApiPreference(nlpQueryResponse);
        List<ApiResult> result = new ArrayList<>();
        if (preferredApi == ApiType.OSM_API) {
            logInfo("Based on the query_object \"" + nlpQueryResponse.getQueryObject() + "\", OSM API will now handle the request.");
            handleOsmApiRequest(nlpQueryResponse, result);
        } else if (preferredApi == ApiType.HERE_API) {
            logInfo("Based on the query_object \"" + nlpQueryResponse.getQueryObject() + "\", HERE API will now handle the request.");
            hereApiRestService.handleRequest(nlpQueryResponse, result);
        } else {
            logError("The preferred API does not match any of our implemented API's!");
            throw new NoPrefferedApiFoundException("No preferred API to use based on this query_object: \"" + nlpQueryResponse.getQueryObject() + "\"");
        }
        adjustTypeValues(result, nlpQueryResponse);
        return result;
    }

    private void handleOsmApiRequest(NlpQueryResponse nlpQueryResponse, List<ApiResult> result) {
        OSMQuery query = generateOsmQuery(nlpQueryResponse);
        OSMSearchResult osmResults = osmApiClient.querySearch(query.toQuery());
        result.addAll(osmResults.getSearchResults());
    }

    // TODO This method contains hard coded values and should be edited
    private OSMQuery generateOsmQuery(NlpQueryResponse nlpQueryResponse) {
        OSMQuery osmQuery = new OSMQuery();
        if (nlpQueryResponse.getQueryObject().equals(NlpQueryResponse.QUERY_OBJECT_ELEVATION)) {
            osmQuery.setNatural("peak");
        } else if (nlpQueryResponse.getQueryObject().equals(NlpQueryResponse.QUERY_OBJECT_PLACE)) {
            osmQuery.setAmenity("restaurant");
        }
        osmQuery.setArea(nlpQueryResponse.getLocation());
        logInfo("Generated Query for OSM: " + osmQuery.toQuery());
        return osmQuery;
    }

    private void adjustTypeValues(List<ApiResult> result, NlpQueryResponse nlpQueryResponse) {
        for (int i = 0; i < result.size(); i++) {
            if (result.get(0).getType().equalsIgnoreCase("Unknown")) {
                result.get(0).setType(nlpQueryResponse.getQueryObject());
            }
        }
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}