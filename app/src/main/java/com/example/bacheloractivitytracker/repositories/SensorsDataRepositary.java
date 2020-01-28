package com.example.bacheloractivitytracker.repositories;

import com.example.bacheloractivitytracker.utils.RxMds;

import io.reactivex.Observable;

public class SensorsDataRepositary {
    private static final String TAG = "SensorsDataRepositary";

    private static SensorsDataRepositary instance;

    public static SensorsDataRepositary getInstance() {
        if (instance == null) {
            instance = new SensorsDataRepositary();
        }
        return instance;
    }

    public Observable<String> subscribeToHeartRate(String serial) {
        return RxMds.Instance.subscribeToHeartRate(serial);
    }

    public Observable<String> subscribeToAcc(String serial, String rate) {
        return RxMds.Instance.subscribeToAcc(serial, rate);
    }

    public Observable<String> subscribeToGyro(String serial, String rate) {
        return RxMds.Instance.subscribeToGyro(serial, rate);
    }
}
