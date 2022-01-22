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

    public String toStringWithPolyline() {
        String output = "ResultResponse: ";
        if (result == null) {
            output += "NULL";
        } else {
            for (ApiResult singleResult : result) {
                output += "\n\t{";
                output += "type=" + singleResult.getType() + ", ";
                output += "name=" + singleResult.getName() + ", ";
                output += "id=" + singleResult.getId() + ", ";
                output += "lat=" + singleResult.getLat() + ", ";
                output += "lon=" + singleResult.getLon() + ", ";
                output += "polyline=" + singleResult.getPolyline();
                output += "}";
            }
        }
        return output;
    }

    public String toStringWithoutPolyline() {
        String output = "ResultResponse: ";
        if (result == null) {
            output += "NULL";
        } else {
            for (ApiResult singleResult : result) {
                output += "\n\t{";
                output += "type=" + singleResult.getType() + ", ";
                output += "name=" + singleResult.getName() + ", ";
                output += "id=" + singleResult.getId() + ", ";
                output += "lat=" + singleResult.getLat() + ", ";
                output += "lon=" + singleResult.getLon() + ", ";
                output += "polyline=NO_PRINT";
                output += "}";
            }
        }
        return output;
    }
}