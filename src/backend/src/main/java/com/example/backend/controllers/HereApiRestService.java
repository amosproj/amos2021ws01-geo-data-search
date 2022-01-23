package com.example.backend.controllers;

import com.example.backend.data.ApiResult;
import com.example.backend.data.api.HereApiRoutingResponse;
import com.example.backend.data.api.HereGuidanceResponse;
import com.example.backend.data.here.*;
import com.example.backend.data.http.NlpQueryResponse;
import com.example.backend.helpers.*;
import com.google.gson.Gson;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class HereApiRestService {

    public static final String HERE_GEOCODE_URL = "https://geocode.search.hereapi.com/v1/geocode";
    public static final String HERE_ROUTING_URL = "https://router.hereapi.com/v8/routes";
    public static final String SEPARATOR = "?";
    public static final String DELIMITER = "&";
    public static final String URL_QUERY_FIELD = "q=";
    public static final String URL_QUERY_TRANSPORT_MODE = "transportMode=";
    public static final String URL_QUERY_ORIGIN = "origin=";
    public static final String URL_QUERY_DESTINATION = "destination=";

    private static final String HERE_API_KEY = HereApiKey.getKey();
    private static final String URL_QUERY_API_KEY = "apiKey=" + HERE_API_KEY;
    public static final String TYPE_FINISH = "Finish";
    public static final String TYPE_START = "Start";

    private final RestTemplate restTemplate;
    private final Logger logger = LogManager.getLogger("HERE_API_REST_SERVICE");

    public HereApiRestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(String query) {
        String url = HERE_GEOCODE_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER + URL_QUERY_FIELD + query;
        logger.info("URL for HERE GEOCODE = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logger.debug("HereApiRestService.getPostsPlainJSON() = " + response);
        return response;
    }

    public void handleRequest(NlpQueryResponse nlpQueryResponse, List<ApiResult> result) throws MissingLocationException, LocationNotFoundException, InvalidCalculationRequest {
        HereRoutingAttributes hereRoutingAttributes = new HereRoutingAttributes(this);
        hereRoutingAttributes.extractRoutingAttributes(nlpQueryResponse);
        logger.info("Searching for a route from \"" + hereRoutingAttributes.getOrigin().getName() + "\" to \"" + hereRoutingAttributes.getDestination().getName() + "\"");
        if (hereRoutingAttributes.getIfChargingStationsIncluded()) {
            result.addAll(getChargingStationsOnRoute(hereRoutingAttributes));
        } else {
            result.addAll(getGuidanceForRoute(hereRoutingAttributes));
        }
    }

    private List<ApiResult> getChargingStationsOnRoute(HereRoutingAttributes hereRoutingAttributes) {
        RoutingWaypoint origin = hereRoutingAttributes.getOrigin();
        RoutingWaypoint destination = hereRoutingAttributes.getDestination();
        List<ApiResult> listOfPointsAlongTheRoute = new ArrayList<>();
        try {
            hereRoutingAttributes.setReturnTypeToPolylineAndTurnByTurnActions();
            String hereApiRoutingResponseString =
                    getRoutingResponse(origin.getCoordinatesAsString(), destination.getCoordinatesAsString(), hereRoutingAttributes);
            logger.debug("HERE / ROUTING / CHARGING STATIONS:");
            logger.debug(hereApiRoutingResponseString);
            HereApiRoutingResponse hereApiRoutingResponse = new Gson().fromJson(hereApiRoutingResponseString, HereApiRoutingResponse.class);
            logger.debug(hereApiRoutingResponse.toString(""));
            Route route = hereApiRoutingResponse.routes.get(0);
            List<Place> chargingStations = new ArrayList<>(route.getAlLChargingStations());
            String polyline = route.sections.get(0).polyline;
            int i = 1;
            listOfPointsAlongTheRoute.add(new SingleLocationResult("Start", 0, origin.getName(), origin.getLatitude(), origin.getLongitude(), polyline));
            int total = chargingStations.size();
            for (Place chargingStation : chargingStations) {
                String type = chargingStation.type;
                String name = "Charging Station " + i + "/" + total;
                String lat = "" + chargingStation.location.lat;
                String lng = "" + chargingStation.location.lng;
                String polylineString = chargingStation.getPolyline();
                listOfPointsAlongTheRoute.add(new SingleLocationResult(type, i, name, lat, lng, polylineString));
                i++;
            }
            listOfPointsAlongTheRoute.add(new SingleLocationResult(TYPE_FINISH, i, destination.getName(), destination.getCoordinatesAsString()));
        } catch (Throwable throwable) {
            logger.error(throwable.toString());
            logger.error(throwable.getMessage());
        }
        return listOfPointsAlongTheRoute;
    }

    private List<ApiResult> getGuidanceForRoute(HereRoutingAttributes hereRoutingAttributes) {
        RoutingWaypoint origin = hereRoutingAttributes.getOrigin();
        RoutingWaypoint destination = hereRoutingAttributes.getDestination();
        List<ApiResult> generalRoutePoints = new ArrayList<>();
        try {
            HereGuidanceResponse hereApiRoutingResponse =
                    getGuidanceResponse(origin.getCoordinatesAsString(), destination.getCoordinatesAsString(), hereRoutingAttributes);
            logger.debug("HERE / GUIDANCE:");
            logger.debug(hereApiRoutingResponse.toString(""));
            for (Route route : hereApiRoutingResponse.routes) {
                for (Section section : route.sections) {
                    String type = TYPE_START;
                    int id = new Random().nextInt();
                    String lat = "" + section.departure.place.location.lat;
                    String lng = "" + section.departure.place.location.lng;
                    String name = origin.getName();
                    generalRoutePoints.add(new SingleLocationResult(type, id, name, lat, lng, section.polyline));
                    type = TYPE_FINISH;
                    id = new Random().nextInt();
                    lat = "" + section.arrival.place.location.lat;
                    lng = "" + section.arrival.place.location.lng;
                    name = destination.getName();
                    generalRoutePoints.add(new SingleLocationResult(type, id, name, lat, lng));
                }
            }
        } catch (Throwable throwable) {
            logger.error(throwable.toString());
            logger.error(throwable.getMessage());
        }
        return generalRoutePoints;
    }

    private String getRoutingResponse(String origin, String destination, HereRoutingAttributes hereRoutingAttributes) {
        String url_query_attributes = hereRoutingAttributes.getUrlArgumentsForRouting();
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER +  //
                URL_QUERY_TRANSPORT_MODE + TransportMode.CAR + DELIMITER + //
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_attributes + //
                URL_QUERY_DESTINATION + destination;
        logger.debug("URL for HERE ROUTING = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logger.debug("HereApiRestService.getRoutingResponse() = " + response);
        return response;
    }

    private HereGuidanceResponse getGuidanceResponse(String origin, String destination, HereRoutingAttributes hereRoutingAttributes) {
        hereRoutingAttributes.setReturnTypeToPolylineAndTurnByTurnActions();
        String url_query_attributes = hereRoutingAttributes.getUrlArgumentsForGuidance();
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER + //
                URL_QUERY_TRANSPORT_MODE + TransportMode.CAR + DELIMITER +
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_attributes + //
                URL_QUERY_DESTINATION + destination;
        logger.debug("URL for HERE GUIDANCE = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logger.debug("HereApiRestService.getGuidanceResponse() = " + response);
        return new Gson().fromJson(response, HereGuidanceResponse.class);
    }

    public HereApiRoutingResponse getRoute(RoutingWaypoint origin, RoutingWaypoint destination) {
        String hereApiRoutingResponseString = getRoutingResponse(origin.getCoordinatesAsString(), destination.getCoordinatesAsString(), new HereRoutingAttributes(this));
        return new Gson().fromJson(hereApiRoutingResponseString, HereApiRoutingResponse.class);
    }

    private static class SingleLocationResult implements ApiResult {

        private String type;
        private final int id;
        private final String lat;
        private final String lon;
        private final String name;
        private String polyline = "";

        public SingleLocationResult(String type, int id, String name, String lat, String lon, String polyline) {
            this.type = type;
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = name;
            this.polyline = polyline;
        }

        public SingleLocationResult(String type, int id, String name, double lat, double lon, String polyline) {
            this.type = type;
            this.id = id;
            this.lat = "" + lat;
            this.lon = "" + lon;
            this.name = name;
            this.polyline = polyline;
        }

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

        @Override
        public String getPolyline() {
            return polyline;
        }
    }
}