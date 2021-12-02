package com.example.backend.controllers;

import com.example.backend.helpers.BackendLogger;
import com.example.backend.helpers.HereApiKey;
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

    public String getRoutingResponse(String origin, String destination, String transportMode, String returnType) {
        String url = HERE_ROUTING_URL + SEPARATOR + URL_QUERY_API_KEY + DELIMITER +  //
                URL_QUERY_TRANSPORT_MODE + transportMode + DELIMITER + //
                URL_QUERY_ORIGIN + origin + DELIMITER + //
                URL_QUERY_DESTINATION + destination + DELIMITER + //
                URL_QUERY_RETURN_TYPE + returnType;
        logInfo("URL for HERE ROUTING = " + url);
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getRoutingResponse() = " + response);
        return response;
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }
}