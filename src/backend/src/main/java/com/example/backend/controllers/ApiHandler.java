package com.example.backend.controllers;

import com.example.backend.BackendLogger;
import com.example.backend.client.ApiClient;
import com.example.backend.data.NodeInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class ApiHandler {

    private final ApiClient apiClient;
    private final BackendLogger logger = new BackendLogger();
    private final String LOG_PREFIX = "[API_CONTROLLER] ";
    private final String dummyNodeID = "305293190";

    public ApiHandler(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public NodeInfo requestNodeInfo(String nodeID) {
        logger.info(LOG_PREFIX+ "Request Info for node:"+nodeID+" from API");
        logger.info(LOG_PREFIX + "Sending data to API...");
        //TODO replace the dummyNodeID with real one when implemented
        NodeInfo apiXMLResponse = apiClient.requestNode(dummyNodeID);
        logger.info(LOG_PREFIX + "SUCCESS!, response from API received:" + apiXMLResponse);
        //TODO replace this with result when implemented
        return apiXMLResponse;
    }
}
