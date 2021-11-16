package com.example.backend.controllers;

import com.example.backend.BackendLogger;
import com.example.backend.client.ApiClient;
import com.example.backend.data.NodeInfo;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class APIController {

    private final ApiClient apiClient;
    private final BackendLogger logger = new BackendLogger();
    private final String LOG_PREFIX = "[API_CONTROLLER] ";
    private final String dummyNodeID = "305293190";

    public APIController(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Request the node info from API
     *
     * @param nodeID API ID of the node
     */
    @GetMapping("/request_node")
    @ResponseBody
    public HttpResponse requestNodeInfo(@RequestBody String nodeID) {
        logger.info(LOG_PREFIX+ "Request Info for node:"+nodeID+" from API");
        try {
            logger.info(LOG_PREFIX + "Sending data to API...");
            //TODO replace the dummyNodeID with real one when implemented
            NodeInfo apiXMLResponse = apiClient.requestNode(dummyNodeID);
            logger.info(LOG_PREFIX + "SUCCESS!, response from API received:" + apiXMLResponse);
            //TODO replace this with result when implemented
            return new ErrorResponse(Error.createError("Not implemented"));
        } catch (Throwable t) {
            logger.error(LOG_PREFIX + "...ERROR!" + "\n" + "\t" + t.getMessage() + "\n" + "\t" + "Could not send data to API, is the API service running? See error above.");
            return new ErrorResponse(Error.createError(t.getMessage()));
        }
    }
}
