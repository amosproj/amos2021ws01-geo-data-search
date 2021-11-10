package com.example.backend.client;

import com.example.backend.data.NlpAnswer;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "api-client", url = "api:8888")
public interface ApiClient {

    @PostMapping("/{request}")
    void sendToApi(@PathVariable(name = "request") NlpAnswer payload);
}