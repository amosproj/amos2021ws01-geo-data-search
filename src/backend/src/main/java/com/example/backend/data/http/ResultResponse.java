package com.example.backend.data.http;

import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@SuppressWarnings("StringConcatenationInLoop")
@ResponseBody
public class ResultResponse implements HttpResponse {

    private static final String SUPER_TAB = "\t\t\t\t\t\t\t\t\t\t\t";
    List<ApiResult> result;
    String fileName = "example.kml";

    public ResultResponse(List<ApiResult> result) {
        this.result = result;
    }

    public List<ApiResult> getResult() {
        return result;
    }

    public String getFileName() {
        return fileName;
    }

    public String toStringWithPolyline() {
        String output = SUPER_TAB + "ResultResponse: ";
        if (result == null) {
            output += "NULL";
        } else {
            output += "\n" + SUPER_TAB;
            output += "fileName=" + fileName;
            for (ApiResult singleResult : result) {
                output += "\n" + SUPER_TAB + "{";
                output += "api=" + singleResult.getApi() + ", ";
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
        String output = SUPER_TAB + "ResultResponse: ";
        if (result == null) {
            output += "NULL";
        } else {
            output += "\n" + SUPER_TAB;
            output += "fileName=" + fileName;
            for (ApiResult singleResult : result) {
                String polyline = singleResult.getPolyline();
                if (!singleResult.getPolyline().isEmpty()) {
                    polyline = polyline.substring(0, (10 % polyline.length()));
                    polyline += "... REST PRINTED IN DEBUG LOGGING MODE";
                }
                output += "\n" + SUPER_TAB + "\t{";
                output += "api=" + singleResult.getApi() + ", ";
                output += "type=" + singleResult.getType() + ", ";
                output += "name=" + singleResult.getName() + ", ";
                output += "id=" + singleResult.getId() + ", ";
                output += "lat=" + singleResult.getLat() + ", ";
                output += "lon=" + singleResult.getLon() + ", ";
                output += "polyline=" + polyline;
                output += "}";
            }
        }
        return output;
    }
}