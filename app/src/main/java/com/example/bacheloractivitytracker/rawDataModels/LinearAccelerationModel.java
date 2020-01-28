package com.example.bacheloractivitytracker.rawDataModels;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

//inner classes has to be public because if not I cant see the getters from outer class
@Getter
public class LinearAccelerationModel {
    @SerializedName("Body")
    private final Body body;

    @SerializedName("Uri")
    private final String uri;

    @SerializedName("Method")
    private final String method;

    public LinearAccelerationModel(Body body, String uri, String method) {
        this.body = body;
        this.uri = uri;
        this.method = method;
    }

    @Getter
    public class Body {
        @SerializedName("Timestamp")
        private final long timestamp;

        @SerializedName("ArrayAcc")
        private final ArrayModel[] array;

        public Body(long timestamp, ArrayModel[] array) {
            this.timestamp = timestamp;
            this.array = array;
        }
    }
}
