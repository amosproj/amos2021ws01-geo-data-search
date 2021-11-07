package com.example.backend.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.backend.client.NlpClient;

@RestController
@RequestMapping("/backend")
public class FrontendController {
    private NlpClient nlpClient;

    public FrontendController(NlpClient nlpClient) {
        this.nlpClient = nlpClient;
    }

    @PostMapping("/post")
    public void printString(@RequestBody String requestBody) {
        System.out.println(requestBody);
        try {
            System.out.println("sending data to NLP");
            this.nlpClient.sendToNlp(requestBody);
        } catch (Throwable t) {
            System.out.println(t.getMessage());
            System.out.println("Could not send data to NLP, is the NLP service running? See error above.");
        }
    }
}
