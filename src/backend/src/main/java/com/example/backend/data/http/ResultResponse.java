package com.example.backend.data.http;

import com.example.backend.data.ApiResult;
import com.example.backend.data.HttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@ResponseBody
public class ResultResponse implements HttpResponse {

    ArrayList<ApiResult> result;

    public ResultResponse(ArrayList<ApiResult> result) {
        this.result = result;
    }

    public ArrayList<ApiResult> getResult() {
        return result;
    }
}
