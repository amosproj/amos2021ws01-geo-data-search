package com.example.backend.controllers;

import com.example.backend.data.*;
import com.example.backend.data.api.NodeInfo;
import com.example.backend.data.http.ExceptionResponse;
import com.example.backend.data.http.NlpResponse;
import com.example.backend.data.http.ResultResponse;
import com.example.backend.helpers.BackendLogger;
import com.example.backend.clients.ApiClient;
import com.example.backend.clients.NlpClient;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    private final NlpClient nlpClient;
    private final BackendLogger logger = new BackendLogger();
    private final ApiController apiController;
    private static final String LOG_PREFIX = "FRONTEND_CONTROLLER";

    public FrontendController(NlpClient nlpClient, ApiClient apiClient) {
        this.nlpClient = nlpClient;
        this.apiController = new ApiController(apiClient);
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

            NodeInfo apiResponse = this.apiController.requestNodeInfo("1234");
            logInfo("INTERPRETED API RESPONSE:");

            ResultResponse apiResultResponse = new ResultResponse();
            apiResultResponse.putApiResult(apiResponse);
            return apiResultResponse;
        } catch (Throwable throwable) {
            logError("...ERROR!" + "\n" + "\t" + throwable.getMessage() + "\n" + "\t" + "Could not send data to NLP, is the NLP service running? See error above.");
            return new ExceptionResponse((Exception) throwable);
        }
    }

    private void logInfo(String logMsg) {
        logger.info(LOG_PREFIX, logMsg);
    }

    private void logError(String logMsg) {
        logger.error(LOG_PREFIX, logMsg);
    }
}