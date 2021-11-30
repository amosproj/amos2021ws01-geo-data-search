package com.example.backend.controllers;

import com.example.backend.helpers.BackendLogger;
import com.example.backend.helpers.HereApiKey;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HereApiRestService {

    private static final String HERE_API_KEY = HereApiKey.getKey();
    private static final String LOG_PREFIX = "HERE_API_REST_SERVICE";
    private final RestTemplate restTemplate;
    private final BackendLogger logger = new BackendLogger();

    public HereApiRestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public String getPostsPlainJSON(String query) {
        String url = "https://geocode.search.hereapi.com/v1/geocode?apiKey=" + HERE_API_KEY + "&q=" + query;
        String response = this.restTemplate.getForObject(url, String.class);
        logInfo("HereApiRestService.getPostsPlainJSON(" + query + ") = " + response);
        return response;
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }
}