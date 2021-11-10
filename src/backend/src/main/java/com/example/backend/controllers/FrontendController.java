package com.example.backend.controllers;

import com.example.backend.BackendLogger;
import com.example.backend.client.ApiClient;
import com.example.backend.client.NlpClient;
import com.example.backend.data.NlpAnswer;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/backend")
public class FrontendController {

    private final NlpClient nlpClient;
    private final ApiClient apiClient;
    private final BackendLogger logger = new BackendLogger();
    private final String LOG_PREFIX = "[FRONTEND_CONTROLLER] ";

    public FrontendController(NlpClient nlpClient, ApiClient apiClient) {
        this.nlpClient = nlpClient;
        this.apiClient = apiClient;
    }

    /**
     * Receives the query from Frontend and forwards it to NLP.
     *
     * @param query the input coming from the Frontend
     */
    @PostMapping("/user_query")
    public void receiveQueryFromFrontend(@RequestBody String query) {
        logger.info(LOG_PREFIX + "New query received! Query = " + query);
        try {
            logger.info(LOG_PREFIX + "Sending data to NLP...");
            nlpClient.sendToNlp(query);
            logger.info(LOG_PREFIX + "...SUCCESS!");
        } catch (Throwable t) {
            logger.error(LOG_PREFIX + "...ERROR!" + "\n" + "\t" + t.getMessage() + "\n" + "\t" + "Could not send data to NLP, is the NLP service running? See error above.");
        }
    }

    /**
     * Receives the response from NLP and forwards it to the API caller.
     *
     * @param answer the output coming from the NLP
     */
    @PostMapping("/nlp_answer")
    @ResponseBody
    public void receiveNlpAnswer(@RequestBody NlpAnswer answer) {
        logger.info(LOG_PREFIX + "NLP answered!" + "\n" + "\t" + "what = " + answer.getWhat() + "\n" + "\t" + "where = " + answer.getWhere() + "\n" + "\t" + "spec = " + answer.getSpec());
        // TODO implement sending to API
        // apiClient.sendToApi(answer);
        logger.error(LOG_PREFIX + "...ERROR!" + "\n" + "\t" + "Could not send data to API, is the API service running?");
    }
}