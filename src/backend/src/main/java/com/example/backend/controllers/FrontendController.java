package com.example.backend.controllers;

import com.example.backend.client.NlpClient;
import org.springframework.web.bind.annotation.*;

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
        this.nlpClient.sendToNlp(requestBody);
    }
}
