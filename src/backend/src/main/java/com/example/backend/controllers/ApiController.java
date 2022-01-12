package com.example.backend.controllers;

import com.example.backend.clients.OsmApiClient;
import com.example.backend.data.ApiResult;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.data.api.OSMQuery;
import com.example.backend.data.api.OSMSearchResult;
import com.example.backend.data.here.HereRoutingAttributes;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.ApiSelectionHelper.ApiType;
import com.example.backend.helpers.BackendLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.example.backend.helpers.ApiSelectionHelper.RequestType;
import static com.example.backend.helpers.ApiSelectionHelper.getInstance;

@RestController
@RequestMapping("/backend")
public class ApiController {

    private final OsmApiClient osmApiClient;
    private final BackendLogger logger = new BackendLogger();
    private static final String LOG_PREFIX = "API_CONTROLLER";
    private static final String dummyNodeID = "305293190";
    private final HereApiRestService hereApiRestService;

    // Brandenburger Tor in Berlin
    public static final String ROUTE_START_COORDINATES = "52.518462144205756,13.373228882261595";

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

    public List<ApiResult> querySearch(NlpQueryResponse nlpQueryResponse) {
        ApiType preferredApi = getInstance().getApiPreference(nlpQueryResponse);
        List<ApiResult> result = new ArrayList<>();
        logInfo("Selected Api: " + preferredApi);
        if (preferredApi == ApiType.OSM_API) {
            handleOsmApiRequest(nlpQueryResponse, result);
        } else if (preferredApi == ApiType.HERE_API) {
            handleHereApiRequest(nlpQueryResponse, result);
        }
        adjustTypeValues(result, nlpQueryResponse);
        return result;
    }

    private void handleOsmApiRequest(NlpQueryResponse nlpQueryResponse, List<ApiResult> result) {
        OSMQuery query = generateOsmQuery(nlpQueryResponse);
        OSMSearchResult osmResults = osmApiClient.querySearch(query.toQuery());
        result.addAll(osmResults.getSearchResults());
    }

    private void handleHereApiRequest(NlpQueryResponse nlpQueryResponse, List<ApiResult> result) {
        HereRoutingAttributes hereRoutingAttributes = new HereRoutingAttributes();
        if (nlpQueryResponse.getRouteAttributes().getTolLRoads()) {
            hereRoutingAttributes.includeTollRoads();
        } else {
            hereRoutingAttributes.excludeTollRoads();
        }

        // Arc de Triomphe de l’Étoile in Paris (EXAMPLE)
        String temporaryRouteEndCoordinates = "48.873970150314705,2.2949678134907785";

        // String position = apiQueryResults.get(0).getLat() + "," + apiQueryResults.get(0).getLon();
        if (nlpQueryResponse.getRouteAttributes().getChargingStations()) {
            logInfo("Searching for a route to " + nlpQueryResponse.getLocation() + " with charging stations...");
            // TODO Replace this workaround and introduce a proper starting location (coordinates)
            hereRoutingAttributes.includeChargingStations();
            result.addAll(getChargingStationsAlongTheWay(ROUTE_START_COORDINATES, temporaryRouteEndCoordinates, hereRoutingAttributes));
        } else {
            logInfo("Searching for a route to " + nlpQueryResponse.getLocation() + " without charging stations...");
            // TODO Replace this workaround and introduce a proper starting location (coordinates)
            hereRoutingAttributes.excludeChargingStations();
            result.addAll(getGuidanceForRoute(ROUTE_START_COORDINATES, temporaryRouteEndCoordinates, hereRoutingAttributes));
        }
        //  String hereApiResponseAsString = hereApiRestService.getPostsPlainJSON(generateHereQuery(nlpQueryResponse));
        // HereApiGeocodeResponse hereResults = new Gson().fromJson(hereApiResponseAsString, HereApiGeocodeResponse.class);
        // result.addAll(hereResults.getSearchResults());
    }

    public List<ApiResult> getChargingStationsAlongTheWay(String origin, String destination, HereRoutingAttributes hereRoutingAttributes) {
        return hereApiRestService.getChargingStationsOnRoute(origin, destination, hereRoutingAttributes);
    }

    public List<ApiResult> getGuidanceForRoute(String origin, String destination, HereRoutingAttributes hereRoutingAttributes) {
        return hereApiRestService.getGuidanceForRoute(origin, destination, hereRoutingAttributes);
    }

    private OSMQuery generateOsmQuery(NlpQueryResponse nlpQueryResponse) {
        OSMQuery osmQuery = new OSMQuery();
        if (getInstance().getRequestType(nlpQueryResponse) == RequestType.ELEVATION) {
            osmQuery.setNatural("peak");
        } else if (getInstance().getRequestType(nlpQueryResponse) == RequestType.PLACE) {
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
}