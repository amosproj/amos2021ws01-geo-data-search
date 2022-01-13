package com.example.backend.data.here;

import com.example.backend.controllers.HereApiRestService;
import com.example.backend.data.api.HereApiGeocodeResponse;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.BackendLogger;
import com.example.backend.helpers.MissingLocationException;
import com.google.gson.Gson;

public class HereRoutingAttributes {

    private static final String LOG_PREFIX = "HERE_ROUTING_ATTRIBUTES";
    private static final String LOCATIONS_SEPARATOR = ",";
    private final static String DELIMITER = "&";

    private static boolean includeChargingStations = false;
    private static boolean avoidTollRoads = true;

    private final BackendLogger logger = new BackendLogger();
    private final HereApiRestService hereApiRestService;

    private String returnType = "";
    private RoutingWaypoint startLocation;
    private RoutingWaypoint finishLocation;

    /**
     * @param hereApiRestService we need this service to get the coordinates for the origin and destination of the requested route
     */
    public HereRoutingAttributes(HereApiRestService hereApiRestService) {
        this.hereApiRestService = hereApiRestService;
    }

    public String getUrlArguments(boolean guidance) {
        String url_query_attributes = "";
        if (includeChargingStations) {
            url_query_attributes += "ev[connectorTypes]=iec62196Type2Combo" + DELIMITER +
                    "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351" + DELIMITER +
                    "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36" + DELIMITER +
                    "ev[auxiliaryConsumption]=1.8" + DELIMITER +
                    "ev[ascent]=9" + DELIMITER +
                    "ev[descent]=4.3" + DELIMITER +
                    "ev[makeReachable]=true" + DELIMITER +
                    "ev[initialCharge]=48" + DELIMITER +
                    "ev[maxCharge]=80" + DELIMITER +
                    "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1" + DELIMITER +
                    "ev[maxChargeAfterChargingStation]=72" + DELIMITER;
        }
        if (avoidTollRoads && guidance) {
            url_query_attributes += "avoid[features]=tollRoad" + DELIMITER;
        }
        url_query_attributes += "return=" + returnType + DELIMITER;
        return url_query_attributes;
    }

    public void setReturnTypeToSummary() {
        returnType = "summary";
    }

    public void setReturnTypeToPolylineAndTurnByTurnActions() {
        returnType = "polyline,turnbyturnactions";
    }

    public boolean getIfChargingStationsIncluded() {
        return includeChargingStations;
    }

    public RoutingWaypoint getOrigin() {
        return startLocation;
    }

    public RoutingWaypoint getDestination() {
        return finishLocation;
    }

    /**
     * Will analyze the NlpQueryResponse and try to extract as much information out of it as possible.
     *
     * @param nlpQueryResponse the answer from NLP containing general routing request
     * @throws MissingLocationException when the NlpQueryResponse contains no location, this exception will be thrown
     */
    public void extractRoutingAttributes(NlpQueryResponse nlpQueryResponse) throws MissingLocationException {
        if (nlpQueryResponse.getLocation() == null || nlpQueryResponse.getLocation().equals("")) {
            logError("No value found for location! Abort!");
            throw new MissingLocationException("The value for \"location\" cannot be empty when trying to calculate a route!");
        }
        extractOriginLocation(nlpQueryResponse);
        extractDestinationLocation(nlpQueryResponse);
        extractTollRoads(nlpQueryResponse);
        extractChargingStations(nlpQueryResponse);
    }

    private void extractOriginLocation(NlpQueryResponse nlpQueryResponse) {
        String[] locations = nlpQueryResponse.getLocation().split(LOCATIONS_SEPARATOR);
        RoutingWaypoint startLocation;
        if (locations.length > 1) {
            startLocation = callHereApiToRetrieveCoordinatesForLocation(locations[1]);
            logInfo("We will take this value as the START of the route: \"" + startLocation.getName() + "\"");
        } else {
            startLocation = new RoutingWaypoint("Berlin");
            startLocation.updateCoordinates(52.52782311436024, 13.386253770286528);
            logInfo("No location found to START our route! We will take this one as default: \"" + startLocation.getName() + "\"");
        }
        this.startLocation = startLocation;
    }

    private void extractDestinationLocation(NlpQueryResponse nlpQueryResponse) {
        String[] locations = nlpQueryResponse.getLocation().split(LOCATIONS_SEPARATOR);
        String nameOfDesiredFinishLocation = locations[0];
        RoutingWaypoint finishLocation = callHereApiToRetrieveCoordinatesForLocation(nameOfDesiredFinishLocation);
        logInfo("We will take this value as the END of the route: \"" + finishLocation.getName() + "\"");
        this.finishLocation = finishLocation;
    }

    private void extractTollRoads(NlpQueryResponse nlpQueryResponse) {
        if (nlpQueryResponse.getRouteAttributes().getTollRoads()) {
            avoidTollRoads = true;
            logInfo("Route will AVOID tolls!");
        } else {
            avoidTollRoads = false;
            logInfo("Route will NOT avoid tolls!");
        }
    }

    private void extractChargingStations(NlpQueryResponse nlpQueryResponse) {
        if (nlpQueryResponse.getRouteAttributes().getChargingStations()) {
            includeChargingStations = true;
            logInfo("Route will INCLUDE charging stations!");
        } else {
            includeChargingStations = false;
            logInfo("Route will NOT include charging stations!");
        }
    }

    private RoutingWaypoint callHereApiToRetrieveCoordinatesForLocation(String nameOfDesiredLocation) {
        String hereApiResponseAsString = hereApiRestService.getPostsPlainJSON(nameOfDesiredLocation);
        HereApiGeocodeResponse hereResults = new Gson().fromJson(hereApiResponseAsString, HereApiGeocodeResponse.class);
        RoutingWaypoint routingWaypoint = new RoutingWaypoint(nameOfDesiredLocation);
        // The first entry has the highest probability of best fulfilling the request:
        routingWaypoint.updateCoordinates(hereResults.getSearchResults().get(0).position.lat, hereResults.getSearchResults().get(0).position.lng);
        return routingWaypoint;
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}