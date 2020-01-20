package com.example.bacheloractivitytracker.utils;

public class Formatter {

    public static String createGetUri(String serial, String getPath) {
        return Path.URI_SCHEME_PREFIX + serial + getPath;
    }

    public static String createSubUri(String serial, String subscribePathUri, String rate) {
        return serial + "/" + subscribePathUri + rate;
    }
}
