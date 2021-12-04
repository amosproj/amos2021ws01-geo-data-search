package com.example.backend.data.here;

import com.google.gson.annotations.SerializedName;

public class Transport {

    @SerializedName("mode")
    public final String mode;

    public Transport(String mode){
        this.mode = mode;
    }


}
