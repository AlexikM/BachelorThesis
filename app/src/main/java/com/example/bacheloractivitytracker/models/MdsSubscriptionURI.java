package com.example.bacheloractivitytracker.models;

import com.example.bacheloractivitytracker.utils.Formatter;
import com.google.gson.annotations.SerializedName;

public class MdsSubscriptionURI {

    @SerializedName("Uri")
    private String uri;


    public MdsSubscriptionURI(String serial, String subscribeUriPath, String rate) {
        this.uri = Formatter.createSubUri(serial, subscribeUriPath, rate);
    }

    public MdsSubscriptionURI(String uri) {
        this.uri = uri;
    }
}
