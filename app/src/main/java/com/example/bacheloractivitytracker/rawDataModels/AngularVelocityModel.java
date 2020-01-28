package com.example.bacheloractivitytracker.rawDataModels;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class AngularVelocityModel {
    @SerializedName("Body")
    private final Body body;

    @SerializedName("Uri")
    private final String uri;

    @SerializedName("Method")
    private final String method;

    public AngularVelocityModel(Body body, String uri, String method) {
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    @Getter
    public class Body {
        @SerializedName("Timestamp")
        private final long timestamp;

        @SerializedName("ArrayGyro")
        private final ArrayModel[] array;

        public Body(long timestamp, ArrayModel[] array) {
            this.timestamp = timestamp;
            this.array = array;
        }
    }

}
