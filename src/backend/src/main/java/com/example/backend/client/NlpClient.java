package com.example.backend.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "nlp-client", url = "nlp:8888")
public interface NlpClient {

    @PostMapping("/{request}")
    void sendToNlp(@PathVariable(name = "request") String payload);
}