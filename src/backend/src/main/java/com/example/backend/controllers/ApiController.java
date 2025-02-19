package com.example.backend.controllers;

import com.example.backend.clients.HereApiRestService;
import com.example.backend.clients.OsmApiClient;
import com.example.backend.api.ApiResult;
import com.example.backend.api.osm.NodeInfo;
import com.example.backend.api.osm.OSMQuery;
import com.example.backend.api.osm.OSMSearchResult;
import com.example.backend.responses.NlpQueryResponse;
import com.example.backend.helpers.*;
import com.example.backend.helpers.ApiSelectionHelper.ApiType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/backend")
public class ApiController {

    private final OsmApiClient osmApiClient;
    // TODO replace the dummyNodeID with real one when implemented
    private static final String dummyNodeID = "305293190";
    private final HereApiRestService hereApiRestService;
    private final Logger logger = LogManager.getLogger("API_CONTROLLER");

    public ApiController(OsmApiClient osmApiClient, HereApiRestService hereApiRestService) {
        this.osmApiClient = osmApiClient;
        this.hereApiRestService = hereApiRestService;
    }

    @Deprecated(since = "Requesting node info with NodeID is not working for now.")
    public NodeInfo requestNodeInfo(String nodeID) {
        logger.info("Request Info for node:" + nodeID + " from API");
        logger.info("Sending data to API...");
        NodeInfo apiXMLResponse = osmApiClient.requestNode(dummyNodeID);
        logger.info("SUCCESS!, response from API received:" + apiXMLResponse);
        return apiXMLResponse;
    }

    /**
     * This method will make the decision, which API will be used and then calls this API
     *
     * @param nlpQueryResponse the answer from NLP containing information from the query
     * @return the results from the called API's
     * @throws MissingLocationException     if Routing is selected and the location is empty, this Exception will be thrown
     * @throws UnknownQueryObjectException  if the queryObject is unknown to us
     * @throws NoPreferredApiFoundException when we could not decide on which API to use
     * @throws LocationNotFoundException    If the location for routing could not be retrieved from NLP-Response
     * @throws InvalidCalculationRequest    If the random route point could not be calculated by our algorithm
     * @throws EmptyResultException         when the response from API was empty
     */
    public List<ApiResult> querySearch(NlpQueryResponse nlpQueryResponse) throws MissingLocationException, UnknownQueryObjectException, NoPreferredApiFoundException, LocationNotFoundException, InvalidCalculationRequest, EmptyResultException {
        ApiType preferredApi = ApiSelectionHelper.getApiPreference(nlpQueryResponse);
        List<ApiResult> result = new ArrayList<>();
        if (preferredApi == ApiType.OSM_API) {
            logger.info("Based on the query_object \"" + nlpQueryResponse.getQueryObject() + "\", OSM API will now handle the request.");
            handleOsmApiRequest(nlpQueryResponse, result);
        } else if (preferredApi == ApiType.HERE_API) {
            logger.info("Based on the query_object \"" + nlpQueryResponse.getQueryObject() + "\", HERE API will now handle the request.");
            hereApiRestService.handleRequest(nlpQueryResponse, result);
        } else {
            logger.error("The preferred API does not match any of our implemented API's!");
            throw new NoPreferredApiFoundException("No preferred API to use based on this query_object: \"" + nlpQueryResponse.getQueryObject() + "\"");
        }
        adjustTypeValues(result, nlpQueryResponse);
        if (result.isEmpty()) {
            throw new EmptyResultException("The \"" + preferredApi.name() + "\" answered with an empty response!");
        }
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
            osmQuery.setHeight(nlpQueryResponse.getRouteAttributes().getHeight());
        } else if (nlpQueryResponse.getQueryObject().equals(NlpQueryResponse.QUERY_OBJECT_PLACE)) {
            osmQuery.setAmenity("restaurant");
        }
        osmQuery.setArea(nlpQueryResponse.getLocation());
        logger.info("Generated Query for OSM: " + osmQuery.toQuery());
        return osmQuery;
    }

    private void adjustTypeValues(List<ApiResult> result, NlpQueryResponse nlpQueryResponse) {
        for (ApiResult apiResult : result) {
            if (apiResult.getType().equalsIgnoreCase("Unknown")
                    || apiResult.getType().equalsIgnoreCase("node")) {
                apiResult.setType(nlpQueryResponse.getQueryObject());
            }
        }
    }
}