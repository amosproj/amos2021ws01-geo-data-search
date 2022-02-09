package com.example.backend.clients;

import com.example.backend.api.osm.NodeInfo;
import com.example.backend.api.osm.OSMSearchResult;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "api-client", url = "http://www.overpass-api.de/api/interpreter")
public interface OsmApiClient {

    @GetMapping("node/{nodeID}")
    NodeInfo requestNode(@PathVariable(name = "nodeID") String payload);

    @RequestMapping(value = "data", method = RequestMethod.GET)
    @ResponseBody
    OSMSearchResult querySearch(@RequestParam("data") String dataQuery);
}