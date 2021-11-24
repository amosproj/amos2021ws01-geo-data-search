package com.example.backend.data.http;

import com.example.backend.data.HttpResponse;
import org.springframework.stereotype.Component;

@Component
public class FakeResult implements HttpResponse {
    private FakeResult() {
    }

    public static FakeResult createResult() {
        return new FakeResult();
    }

    public String getType() {
        return "Mountain";
    }

    public int getId() {
        return 1234;
    }
    public String getLat() {
        return "12.25";
    }
    public String getLon() {
        return "13.49";
    }
    public String getName() {
        return "Ochsenkopf";
    }
}
