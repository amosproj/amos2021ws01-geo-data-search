package com.example.backend.data.http;

import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@ResponseBody
public class ResultResponse implements HttpResponse {

    List<ApiResult> result;

    public ResultResponse(List<ApiResult> result) {
        this.result = result;
    }

    public List<ApiResult> getResult() {
        return result;
    }

    public String toString() {
        String output = "ResultResponse:\n";
        for (ApiResult singleResult : result) {
            output += "\t{";
            output += "type=" + singleResult.getType() + ", ";
            output += "name=" + singleResult.getName() + ", ";
            output += "id=" + singleResult.getId() + ", ";
            output += "lat=" + singleResult.getLat() + ", ";
            output += "lon=" + singleResult.getLon();
            output += "},\n";
        }
        return output;
    }
}