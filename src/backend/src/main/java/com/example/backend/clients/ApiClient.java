package com.example.backend.clients;

import com.example.backend.data.api.NodeInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "api-client", url = "https://www.openstreetmap.org/api/0.6/")
public interface ApiClient {

    @GetMapping("node/{nodeID}")
    NodeInfo requestNode(@PathVariable(name = "nodeID") String payload);
}