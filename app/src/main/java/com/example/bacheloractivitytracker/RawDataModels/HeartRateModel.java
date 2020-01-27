package com.example.bacheloractivitytracker.RawDataModels;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class HeartRateModel {


    @SerializedName("Body")
    private final Body body;

    @SerializedName("Uri")
    private final String uri;

    @SerializedName("Method")
    private final String method;

    public HeartRateModel(Body body, String uri, String method) {
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    @Getter
    public class Body {
        private final float average;
        private final int[] rrData;

        public Body(float average, int[] rrData) {
            this.average = average;
            this.rrData = rrData;
        }
    }
}

