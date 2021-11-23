package com.example.backend.controllers;

import com.example.backend.BackendLogger;
import com.example.backend.client.ApiClient;
import com.example.backend.client.NlpClient;
import com.example.backend.data.NlpResponse;
import com.example.backend.data.NodeInfo;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    private final NlpClient nlpClient;
    private final BackendLogger logger = new BackendLogger();
    private final ApiHandler apiHandler;
    private static final String LOG_PREFIX = "FRONTEND_CONTROLLER";

    public FrontendController(NlpClient nlpClient, ApiClient apiClient) {
        this.nlpClient = nlpClient;
        this.apiHandler = new ApiHandler(apiClient);
    }

    /**
     * Receives the query from Frontend and forwards it to NLP
     *
     * @param query the input coming from the Frontend
     */
    @PostMapping("/user_query")
    @ResponseBody
    public HttpResponse receiveQueryFromFrontend(@RequestBody String query) {
        logInfo("New query received! Query = " + query);
        try {
            logInfo("Sending data to NLP...");
            String nlpResponse = nlpClient.sendToNlp(query);
            logInfo("...SUCCESS!, response from NLP received:" + nlpResponse);

            Gson g = new Gson();
            logInfo("NLP RESPONSE:");
            logInfo(nlpResponse);
            logInfo("INTERPRETED NLP RESPONSE:");
            logInfo(g.fromJson(nlpResponse, NlpResponse.class).toString());

            NodeInfo apiResponse = this.apiHandler.requestNodeInfo("1234");
            logInfo("INTERPRETED API RESPONSE:");
            logInfo(apiResponse.toString());

            //TODO replace this with result when implemented
            return new ErrorResponse(Error.createError("Not implemented"));
        } catch (Throwable t) {
            logError("...ERROR!" + "\n" + "\t" + t.getMessage() + "\n" + "\t" + "Could not send data to NLP, is the NLP service running? See error above.");
            return new ErrorResponse(Error.createError(t.getMessage()));
        }
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}