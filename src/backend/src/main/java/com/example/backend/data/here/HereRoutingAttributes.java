package com.example.backend.data.here;

import com.example.backend.controllers.HereApiRestService;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.InvalidCalculationRequest;
import com.example.backend.helpers.LocationNotFoundException;
import com.example.backend.helpers.MissingLocationException;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HereRoutingAttributes {

    private final static String DELIMITER = "&";
    private static final RoutingWaypoint DEFAULT_START_LOCATION = new RoutingWaypoint("IAV GmbH", 52.522876680682884, 13.321046275244608);

    private static boolean includeChargingStations = false;
    private static boolean avoidTollRoads = false;

    private final HereApiRestService hereApiRestService;
    private final Logger logger = LogManager.getLogger("HERE_ROUTING_ATTRIBUTES");

    private String returnType = "summary";
    private RoutingWaypoint startLocation;
    private RoutingWaypoint finishLocation;

    /**
     * @param hereApiRestService we need this service to get the coordinates for the origin and destination of the requested route
     */
    public HereRoutingAttributes(HereApiRestService hereApiRestService) {
        this.hereApiRestService = hereApiRestService;
    }

    public String getUrlArgumentsForGuidance() {
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
        if (avoidTollRoads) {
            url_query_attributes += "avoid[features]=tollRoad" + DELIMITER;
        }
        url_query_attributes += "return=" + returnType + DELIMITER;
        return url_query_attributes;
    }

    public String getUrlArgumentsForRouting() {
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
        if (avoidTollRoads) {
            url_query_attributes += "mode=tollroad:-3" + DELIMITER;
        }
        url_query_attributes += "return=" + returnType + DELIMITER;
        return url_query_attributes;
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
    public void extractRoutingAttributes(NlpQueryResponse nlpQueryResponse) throws MissingLocationException, LocationNotFoundException, InvalidCalculationRequest {
        extractOriginLocation(nlpQueryResponse);
        extractDestinationLocation(nlpQueryResponse);
        extractTollRoads(nlpQueryResponse);
        extractChargingStations(nlpQueryResponse);
    }

    private boolean checkIfRouteAttributesMinMaxAreSet(NlpQueryResponse nlpQueryResponse) {
        int maxLength = nlpQueryResponse.getRouteAttributes().getLength().getMax();
        int minLength = nlpQueryResponse.getRouteAttributes().getLength().getMin();
        if (maxLength != 0) {
            logger.info("NlpQueryResponse includes specified RouteAttributes: route max length = " + maxLength);
            return true;
        }
        if (minLength != 0) {
            logger.info("NlpQueryResponse includes specified RouteAttributes: route min length = " + minLength);
            return true;
        }
        return false;
    }

    private void extractOriginLocation(NlpQueryResponse nlpQueryResponse) throws LocationNotFoundException {
        String extractedOriginLocation = nlpQueryResponse.getRouteAttributes().getRouteOrigin();
        RoutingWaypoint startLocation;
        if(extractedOriginLocation != null && !extractedOriginLocation.isEmpty()){
            startLocation = callHereApiToRetrieveCoordinatesForLocation(extractedOriginLocation);
            logger.info("We will take this value as the START of the route: \"" + startLocation.getName() + "\"");
        }else{
            startLocation = DEFAULT_START_LOCATION;
            logger.info("No location found to START our route! We will take this one as default: \"" + startLocation.getName() + "\"");
        }
        this.startLocation = startLocation;
    }

    private void extractDestinationLocation(NlpQueryResponse nlpQueryResponse) throws LocationNotFoundException, InvalidCalculationRequest, MissingLocationException {
        String extractedDestinationLocation = nlpQueryResponse.getRouteAttributes().getRouteDestination();
        if (checkIfRouteAttributesMinMaxAreSet(nlpQueryResponse)) {
            RouteAttributesCalculator rac = new RouteAttributesCalculator(hereApiRestService);
            this.finishLocation = rac.getDestinationOnGivenRouteAttributes(startLocation, nlpQueryResponse.getRouteAttributes());
            this.finishLocation.setName("Random destination");
        } else if (extractedDestinationLocation != null && !extractedDestinationLocation.isEmpty()) {
            this.finishLocation = callHereApiToRetrieveCoordinatesForLocation(extractedDestinationLocation);
        } else {
            throw new MissingLocationException("Destination location missing! No route calculation possible!");
        }
        logger.info("We will take this value as the END of the route: \"" + finishLocation.getName() + "\"");
    }

    private void extractTollRoads(NlpQueryResponse nlpQueryResponse) {
        if (nlpQueryResponse.getRouteAttributes().shouldTollRoutesBeAvoided()) {
            avoidTollRoads = true;
            logger.info("Route will AVOID tolls!");
        } else {
            avoidTollRoads = false;
            logger.info("Route will NOT avoid tolls!");
        }
    }

    private void extractChargingStations(NlpQueryResponse nlpQueryResponse) {
        if (nlpQueryResponse.getRouteAttributes().getChargingStations()) {
            includeChargingStations = true;
            logger.info("Route will INCLUDE charging stations!");
        } else {
            includeChargingStations = false;
            logger.info("Route will NOT include charging stations!");
        }
    }

    private RoutingWaypoint callHereApiToRetrieveCoordinatesForLocation(String nameOfDesiredLocation) throws LocationNotFoundException {
        String hereApiResponseAsString = hereApiRestService.getPostsPlainJSON(nameOfDesiredLocation.trim());
        HereApiGeocodeResponse hereResults = new Gson().fromJson(hereApiResponseAsString, HereApiGeocodeResponse.class);
        if (hereResults.items.isEmpty()) {
            throw new LocationNotFoundException("HERE API GEOCODE could not find this location: \"" + nameOfDesiredLocation + "\"!");
        }
        RoutingWaypoint routingWaypoint = new RoutingWaypoint(nameOfDesiredLocation);
        // The first entry has the highest probability of best fulfilling the request:
        routingWaypoint.updateCoordinates(hereResults.getSearchResults().get(0).position.lat, hereResults.getSearchResults().get(0).position.lng);
        return routingWaypoint;
    }
}