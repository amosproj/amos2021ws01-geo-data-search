package com.example.backend.controllers;

import com.example.backend.data.ApiResult;
import com.example.backend.data.api.HereApiRoutingResponse;
import com.example.backend.data.api.HereGuidanceResponse;
import com.example.backend.data.here.Place;
import com.example.backend.data.here.Route;
import com.example.backend.data.here.Section;
import com.example.backend.data.here.TransportMode;
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
    public static final String URL_QUERY_RETURN_TYPE = "return=";
    private final RestTemplate restTemplate;
    private final BackendLogger logger = new BackendLogger();
    public static final String RETURN_TYPE_SUMMARY = "summary";

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

    public String getRoutingResponse(String origin, String destination, String transportMode, String returnType, boolean ev) {
        String url_query_ev = "";
        if (ev) {
            url_query_ev = "ev[connectorTypes]=iec62196Type2Combo&" +
                    "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351&" +
                    "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36&" +
                    "ev[auxiliaryConsumption]=1.8&" +
                    "ev[ascent]=9&" +
                    "ev[descent]=4.3&" +
                    "ev[makeReachable]=true&" +
                    "ev[initialCharge]=48&" +
                    "ev[maxCharge]=80&" +
                    "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1&" +
                    "ev[maxChargeAfterChargingStation]=72&";
        }
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER +  //
                URL_QUERY_TRANSPORT_MODE + transportMode + DELIMITER + //
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_ev + //
                URL_QUERY_DESTINATION + destination + DELIMITER + //
                URL_QUERY_RETURN_TYPE + returnType;
        logInfo("URL for HERE ROUTING = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getRoutingResponse() = " + response);
        return response;
    }

    public HereGuidanceResponse getGuidanceResponse(String origin, String destination, String transportMode, boolean ev) {
        String url_query_ev = "";
        if (ev) {
            url_query_ev = "ev[connectorTypes]=iec62196Type2Combo&" +
                    "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351&" +
                    "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36&" +
                    "ev[auxiliaryConsumption]=1.8&" +
                    "ev[ascent]=9&" +
                    "ev[descent]=4.3&" +
                    "ev[makeReachable]=true&" +
                    "ev[initialCharge]=48&" +
                    "ev[maxCharge]=80&" +
                    "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1&" +
                    "ev[maxChargeAfterChargingStation]=72&";
        }
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER + //
                URL_QUERY_TRANSPORT_MODE + transportMode + DELIMITER +
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                url_query_ev + //
                URL_QUERY_DESTINATION + destination + DELIMITER + //
                URL_QUERY_RETURN_TYPE + "polyline,turnbyturnactions";
        logInfo("URL for HERE GUIDANCE = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getGuidanceResponse() = " + response);
        return new Gson().fromJson(response, HereGuidanceResponse.class);
    }

    public List<ApiResult> getChargingStationsOnRoute(String origin, String destination) {
        List<ApiResult> listOfChargingStations = new ArrayList<>();
        try {
            String hereApiRoutingResponseString =
                    getRoutingResponse(origin, destination, TransportMode.CAR, RETURN_TYPE_SUMMARY, true);
            logInfo("HERE / ROUTING / CHARGING STATIONS:");
            logInfo(hereApiRoutingResponseString);
            HereApiRoutingResponse hereApiRoutingResponse = new Gson().fromJson(hereApiRoutingResponseString, HereApiRoutingResponse.class);
            logInfo(hereApiRoutingResponse.toString(""));
            List<Place> chargingStations = new ArrayList<Place>();
            for (Route route : hereApiRoutingResponse.routes) {
                chargingStations.addAll(route.getAlLChargingStations());
            }
            logInfo("A selection of charging stations found between " + origin + " and " + destination + ":");
            int i = 1;
            int size = chargingStations.size();
            for (Place chargingStation : chargingStations) {
                logInfo("ChargingStation " + i + "/" + size + ": " + chargingStation.toString(""));
                String lat = "" + chargingStation.location.lat;
                String lng = "" + chargingStation.location.lng;
                String name = "Charging Station";
                int id = i - 1;
                String type = chargingStation.type;
                listOfChargingStations.add(new SingleLocationResult(type, id, lat, lng, name));
                i++;
            }
        } catch (Throwable throwable) {
            logError(throwable.toString());
            logError(throwable.getMessage());
        }
        return listOfChargingStations;
    }


    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }

    // TODO Proper way of sending guidance to FrontEnd is still not established
    public List<ApiResult> getGuidanceForRoute(String origin, String destination, boolean includingChargingStations) {
        List<ApiResult> generalRoutePoints = new ArrayList<>();

        try {
            HereGuidanceResponse hereApiRoutingResponse =
                    getGuidanceResponse(origin, destination, TransportMode.CAR, includingChargingStations);
            logInfo("HERE / GUIDANCE:");
            logInfo(hereApiRoutingResponse.toString(""));
            for (Route route : hereApiRoutingResponse.routes) {
                for (Section section : route.sections) {
                    String type = section.departure.place.type;
                    int id = new Random().nextInt();
                    String lat = "" + section.departure.place.location.lat;
                    String lng = "" + section.departure.place.location.lng;
                    String name = origin;
                    generalRoutePoints.add(new SingleLocationResult(type, id, lat, lng, name));
                    type = section.arrival.place.type;
                    id = new Random().nextInt();
                    lat = "" + section.arrival.place.location.lat;
                    lng = "" + section.arrival.place.location.lng;
                    name = destination;
                    generalRoutePoints.add(new SingleLocationResult(type, id, lat, lng, name));
                }
            }
        } catch (Throwable throwable) {
            logError(throwable.toString());
            logError(throwable.getMessage());
        }
        return generalRoutePoints;
    }

    private static class SingleLocationResult implements ApiResult {

        private String type;
        private final int id;
        private final String lat;
        private final String lon;
        private final String name;

        public SingleLocationResult(String type, int id, String lat, String lon, String name) {
            this.type = type;
            this.id = id;
            this.lat = lat;
            this.lon = lon;
            this.name = name;
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
}