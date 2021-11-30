package com.example.backend.controllers;

import com.example.backend.clients.ApiClient;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.helpers.BackendLogger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend")
public class ApiController {

    private final ApiClient apiClient;
    private final BackendLogger logger = new BackendLogger();
    private static final String LOG_PREFIX = "API_CONTROLLER";
    private static final String dummyNodeID = "305293190";

    public ApiController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public NodeInfo requestNodeInfo(String nodeID) {
        logInfo("Request Info for node:" + nodeID + " from API");
        logInfo("Sending data to API...");
        //TODO replace the dummyNodeID with real one when implemented
        NodeInfo apiXMLResponse = apiClient.requestNode(dummyNodeID);
        logInfo("SUCCESS!, response from API received:" + apiXMLResponse);
        //TODO replace this with result when implemented
        return apiXMLResponse;
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }
}