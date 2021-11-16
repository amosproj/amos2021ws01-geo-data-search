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
            return new ErrorResponse(Error.createError("Not implemented"));
        } catch (Throwable t) {
            logger.error(LOG_PREFIX + "...ERROR!" + "\n" + "\t" + t.getMessage() + "\n" + "\t" + "Could not send data to NLP, is the NLP service running? See error above.");
            return new ErrorResponse(Error.createError(t.getMessage()));
        }
    }

    /**
     * Receives the response from NLP and forwards it to the API caller.
     *
     * @param answer the output coming from the NLP
     */
    @GetMapping("/nlp_answer")
    @ResponseBody
    public HttpResponse receiveNlpAnswer(@RequestBody NlpAnswer answer) {
        logger.info(LOG_PREFIX + "NLP answered!" + "\n" + "\t" + "what = " + answer.getWhat() + "\n" + "\t" + "where = " + answer.getWhere() + "\n" + "\t" + "spec = " + answer.getSpec());
        return apiController.requestNodeInfo(answer.getSpec());
    }
}
