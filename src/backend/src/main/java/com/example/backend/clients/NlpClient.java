package com.example.backend.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "nlp-client", url = "nlp:8000")
public interface NlpClient {

    @GetMapping("/request/{request}")
    String sendToNlp(@PathVariable(name = "request") String payload);

    @GetMapping("/version/")
    String fetchNlpVersion();
}
