package com.example.backend.api.osm;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@ResponseBody
public class OSMSearchResult {
    @JsonProperty("elements")
    private ArrayList<NodeInfo> searchResults;

    public ArrayList<NodeInfo> getSearchResults() {
        return searchResults;
    }

    @SuppressWarnings("unused")
    public void setSearchResults(ArrayList<NodeInfo> searchResults) {
        this.searchResults = searchResults;
    }

    @Override
    public String toString() {
        return "OSMSearchResult{" +
                "searchResults=" + searchResults +
                '}';
    }
}
