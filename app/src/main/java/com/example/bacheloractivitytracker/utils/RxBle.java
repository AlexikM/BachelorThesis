package com.example.bacheloractivitytracker.utils;

import android.content.Context;

import com.polidea.rxandroidble2.RxBleClient;

//TODO singleton predelat using dagger!!

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
