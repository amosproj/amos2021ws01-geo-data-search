package com.example.backend.controllers;

import com.example.backend.data.ApiResult;
import com.example.backend.data.api.HereApiRoutingResponse;
import com.example.backend.data.api.HereGuidanceResponse;
import com.example.backend.data.here.*;
import com.example.backend.helpers.BackendLogger;
import com.example.backend.helpers.HereApiKey;
import com.google.gson.Gson;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HereApiRestService {

    private static final String HERE_API_KEY = HereApiKey.getKey();
    public static final String URL_QUERY_API_KEY = "apiKey=" + HERE_API_KEY;
    private static final String LOG_PREFIX = "HERE_API_REST_SERVICE";
    public static final String HERE_GEOCODE_URL = "https://geocode.search.hereapi.com/v1/geocode";
    public static final String HERE_ROUTING_URL = "https://router.hereapi.com/v8/routes";
    public static final String SEPARATOR = "?";
    public static final String DELIMITER = "&";
    public static final String URL_QUERY_FIELD = "q=";
    public static final String URL_QUERY_TRANSPORT_MODE = "transportMode=";
    public static final String URL_QUERY_ORIGIN = "origin=";
    public static final String URL_QUERY_DESTINATION = "destination=";
    private final RestTemplate restTemplate;
    private final BackendLogger logger = new BackendLogger();

    public HereApiRestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(String query) {
        String url = HERE_GEOCODE_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER + URL_QUERY_FIELD + query;
        logInfo("URL for HERE GEOCODE = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getPostsPlainJSON() = " + response);
        return response;
    }

    /**
     * @param hereRoutingAttributes containing general routing request information
     * @return a list of ApiResult containing the origin, a selection of charging stations along the way and the destination
     */
    public List<ApiResult> getChargingStationsOnRoute(HereRoutingAttributes hereRoutingAttributes) {
        RoutingWaypoint origin = hereRoutingAttributes.getOrigin();
        RoutingWaypoint destination = hereRoutingAttributes.getDestination();
        List<ApiResult> listOfPointsAlongTheRoute = new ArrayList<>();
        try {
            hereRoutingAttributes.setReturnTypeToSummary();
            String hereApiRoutingResponseString =
                    getRoutingResponse(origin.getCoordinatesAsString(), destination.getCoordinatesAsString(), TransportMode.CAR, hereRoutingAttributes);
            logInfo("HERE / ROUTING / CHARGING STATIONS:");
            logInfo(hereApiRoutingResponseString);
            HereApiRoutingResponse hereApiRoutingResponse = new Gson().fromJson(hereApiRoutingResponseString, HereApiRoutingResponse.class);
            logInfo(hereApiRoutingResponse.toString(""));
            List<Place> chargingStations = new ArrayList<>();
            for (Route route : hereApiRoutingResponse.routes) {
                chargingStations.addAll(route.getAlLChargingStations());
            }
            logInfo("A selection of charging stations found between " + origin.getName() + " and " + destination.getName() + ":");
            int i = 1;
            listOfPointsAlongTheRoute.add(new SingleLocationResult("Start", 0, origin.getName(), origin.getCoordinatesAsString()));
            int total = chargingStations.size();
            for (Place chargingStation : chargingStations) {
                String type = chargingStation.type;
                String name = "Charging Station " + i + "/" + total;
                String lat = "" + chargingStation.location.lat;
                String lng = "" + chargingStation.location.lng;
                listOfPointsAlongTheRoute.add(new SingleLocationResult(type, i, name, lat, lng));
                i++;
            }
            listOfPointsAlongTheRoute.add(new SingleLocationResult("Finish", i, destination.getName(), destination.getCoordinatesAsString()));
        } catch (Throwable throwable) {
            logError(throwable.toString());
            logError(throwable.getMessage());
        }
        return listOfPointsAlongTheRoute;
    }

    /**
     * @param hereRoutingAttributes containing general routing request information
     * @return a list of ApiResult containing the origin and the destination
     */
    public List<ApiResult> getGuidanceForRoute(HereRoutingAttributes hereRoutingAttributes) {
        RoutingWaypoint origin = hereRoutingAttributes.getOrigin();
        RoutingWaypoint destination = hereRoutingAttributes.getDestination();
        List<ApiResult> generalRoutePoints = new ArrayList<>();
        try {
            HereGuidanceResponse hereApiRoutingResponse =
                    getGuidanceResponse(origin.getCoordinatesAsString(), destination.getCoordinatesAsString(), TransportMode.CAR, hereRoutingAttributes);
            logInfo("HERE / GUIDANCE:");
            logInfo(hereApiRoutingResponse.toString(""));
            for (Route route : hereApiRoutingResponse.routes) {
                for (Section section : route.sections) {
                    String type = "Start";
                    int id = new Random().nextInt();
                    String lat = "" + section.departure.place.location.lat;
                    String lng = "" + section.departure.place.location.lng;
                    String name = origin.getName();
                    generalRoutePoints.add(new SingleLocationResult(type, id, name, lat, lng));
                    type = "Finish";
                    id = new Random().nextInt();
                    lat = "" + section.arrival.place.location.lat;
                    lng = "" + section.arrival.place.location.lng;
                    name = destination.getName();
                    generalRoutePoints.add(new SingleLocationResult(type, id, name, lat, lng));
                }
            }
        } catch (Throwable throwable) {
            logError(throwable.toString());
            logError(throwable.getMessage());
        }
        return generalRoutePoints;
    }

    private String getRoutingResponse(String origin, String destination, String transportMode, HereRoutingAttributes hereRoutingAttributes) {
        String url_query_attributes = hereRoutingAttributes.getUrlArguments(false);
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER +  //
                URL_QUERY_TRANSPORT_MODE + transportMode + DELIMITER + //
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_attributes + //
                URL_QUERY_DESTINATION + destination;
        logInfo("URL for HERE ROUTING = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getRoutingResponse() = " + response);
        return response;
    }

    private HereGuidanceResponse getGuidanceResponse(String origin, String destination, String transportMode, HereRoutingAttributes hereRoutingAttributes) {
        hereRoutingAttributes.setReturnTypeToPolylineAndTurnByTurnActions();
        String url_query_attributes = hereRoutingAttributes.getUrlArguments(true);
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER + //
                URL_QUERY_TRANSPORT_MODE + transportMode + DELIMITER +
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_attributes + //
                URL_QUERY_DESTINATION + destination;
        logInfo("URL for HERE GUIDANCE = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getGuidanceResponse() = " + "DEACTIVATED PRINT OUTS NOW");
        return new Gson().fromJson(response, HereGuidanceResponse.class);
    }

    private static class SingleLocationResult implements ApiResult {

        private String type;
        private final int id;
        private final String lat;
        private final String lon;
        private final String name;

        public SingleLocationResult(String type, int id, String name, String lat, String lon) {
            this.type = type;
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = name;
        }

        public SingleLocationResult(String type, int id, String name, String coordinates) {
            this.type = type;
            this.id = id;
            this.name = name;
            String[] coordinatesArray = coordinates.split(",");
            this.lat = coordinatesArray[0];
            this.lon = coordinatesArray[1];
        }

        @Override
        public String getType() {
            return type;
        }

        @java.lang.Override
        public void setType(String type) {
            this.type = type;
        }

        @Override
        public int getId() {
            return id;
        }

        @Override
        public String getLat() {
            return lat;
        }

        @Override
        public String getLon() {
            return lon;
        }

        @Override
        public String getName() {
            return name;
        }
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}