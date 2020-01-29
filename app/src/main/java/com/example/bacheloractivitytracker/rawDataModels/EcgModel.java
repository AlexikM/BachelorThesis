package com.example.bacheloractivitytracker.rawDataModels;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class EcgModel {

    @SerializedName("Body")
    private final Body body;

    @SerializedName("Uri")
    private final String uri;

    @SerializedName("Method")
    private final String method;

    public EcgModel(Body body, String uri, String method) {
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    @Getter
    public class Body {
        @SerializedName("Samples")
        private final int[] samples;

        @SerializedName("Timestamp")
        private final long timestamp;

        public Body(int[] samples, long timestamp) {
            this.samples = samples;
            this.timestamp = timestamp;
        }
    }
}
