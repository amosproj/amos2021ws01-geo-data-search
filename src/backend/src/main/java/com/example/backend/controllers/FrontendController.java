package com.example.backend.controllers;

import com.example.backend.BackendLogger;
import com.example.backend.client.ApiClient;
import com.example.backend.client.NlpClient;
import com.example.backend.data.NlpResponse;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    private final NlpClient nlpClient;
    private final BackendLogger logger = new BackendLogger();
    private final APIController apiController;
    private final String LOG_PREFIX = "[FRONTEND_CONTROLLER] ";

    public FrontendController(NlpClient nlpClient, ApiClient apiClient) {
        this.nlpClient = nlpClient;
        this.apiController = new APIController(apiClient);
    }

    /**
     * Receives the query from Frontend and forwards it to NLP
     *
     * @param query the input coming from the Frontend
     */
    @PostMapping("/user_query")
    @ResponseBody
    public HttpResponse receiveQueryFromFrontend(@RequestBody String query) {
        logger.info(LOG_PREFIX + "New query received! Query = " + query);
        try {
            logger.info(LOG_PREFIX + "Sending data to NLP...");
            String nlpResponse = nlpClient.sendToNlp(query);
            logger.info(LOG_PREFIX + "...SUCCESS!, response from NLP received:" + nlpResponse);
            //TODO replace this with result when implemented
            //NlpResponse r = new NlpResponse(nlpResponse, , )
            Gson g = new Gson();
            System.out.println("RESPONSE: \n");
            System.out.println(nlpResponse);
            System.out.println("JSONIFIED RESPONSE: \n");
            System.out.println(g.fromJson(nlpResponse, NlpResponse.class));

            return new ErrorResponse(Error.createError("Not implemented"));
        } catch (Throwable t) {
            logger.error(LOG_PREFIX + "...ERROR!" + "\n" + "\t" + t.getMessage() + "\n" + "\t" + "Could not send data to NLP, is the NLP service running? See error above.");
            return new ErrorResponse(Error.createError(t.getMessage()));
        }
    }
}
