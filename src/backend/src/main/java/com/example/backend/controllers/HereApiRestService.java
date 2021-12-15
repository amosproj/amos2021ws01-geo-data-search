package com.example.backend.controllers;

import com.example.backend.data.api.HereApiRoutingResponse;
import com.example.backend.data.api.HereGuidanceResponse;
import com.example.backend.data.here.TransportMode;
import com.example.backend.helpers.BackendLogger;
import com.example.backend.helpers.HereApiKey;
import com.google.gson.Gson;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
            url_query_ev += "ev[connectorTypes]=iec62196Type2Combo&";
            url_query_ev += "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351&";
            url_query_ev += "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36&";
            url_query_ev += "ev[auxiliaryConsumption]=1.8&";
            url_query_ev += "ev[ascent]=9&";
            url_query_ev += "ev[descent]=4.3&";
            url_query_ev += "ev[makeReachable]=true&";
            url_query_ev += "ev[initialCharge]=48&";
            url_query_ev += "ev[maxCharge]=80&";
            url_query_ev += "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1&";
            url_query_ev += "ev[maxChargeAfterChargingStation]=72&";
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
            url_query_ev += "ev[connectorTypes]=iec62196Type2Combo&";
            url_query_ev += "ev[freeFlowSpeedTable]=0,0.239,27,0.239,45,0.259,60,0.196,75,0.207,90,0.238,100,0.26,110,0.296,120,0.337,130,0.351,250,0.351&";
            url_query_ev += "ev[trafficSpeedTable]=0,0.349,27,0.319,45,0.329,60,0.266,75,0.287,90,0.318,100,0.33,110,0.335,120,0.35,130,0.36,250,0.36&";
            url_query_ev += "ev[auxiliaryConsumption]=1.8&";
            url_query_ev += "ev[ascent]=9&";
            url_query_ev += "ev[descent]=4.3&";
            url_query_ev += "ev[makeReachable]=true&";
            url_query_ev += "ev[initialCharge]=48&";
            url_query_ev += "ev[maxCharge]=80&";
            url_query_ev += "ev[chargingCurve]=0,239,32,199,56,167,60,130,64,111,68,83,72,55,76,33,78,17,80,1&";
            url_query_ev += "ev[maxChargeAfterChargingStation]=72&";
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

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }
}