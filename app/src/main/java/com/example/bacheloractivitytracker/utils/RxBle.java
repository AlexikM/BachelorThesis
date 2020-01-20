package com.example.bacheloractivitytracker.utils;

import android.content.Context;

import com.polidea.rxandroidble2.RxBleClient;

//TODO singleton predelat using dagger!!


/*
    Class for searching for the ble devices around
 */
public enum RxBle {
    Instance;

    private RxBleClient client;

    public void initialize(Context context) {
        client = RxBleClient.create(context);
    }

    public RxBleClient getClient() {
        return client;
    }


}
