package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;

@ResponseBody
public class ResultResponse implements HttpResponse {

    ArrayList<FakeResult> result;

    public ResultResponse(ArrayList<FakeResult> result) {
        this.result = result;
    }

    public ArrayList<FakeResult> getResult() {
        return result;
    }
}
