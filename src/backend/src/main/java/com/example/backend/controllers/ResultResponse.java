package com.example.backend.controllers;

import com.example.backend.data.ApiResult;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@ResponseBody
public class ResultResponse implements HttpResponse {
    List<ApiResult> apiResults;

    public ResultResponse() {
        apiResults = new ArrayList<>();
    }

    public void putApiResult(ApiResult apiResult) {
        if (!apiResults.contains(apiResult)) {
            apiResults.add(apiResult);
        }
    }

    public void removeApiResult(ApiResult apiResult) {
        apiResults.remove(apiResult);
    }
}
