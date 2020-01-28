package com.example.bacheloractivitytracker.models;


import android.util.Log;

import com.example.bacheloractivitytracker.rawDataModels.HeartRateModel;
import com.example.bacheloractivitytracker.adapters.DashboardRecyclerAdapter;
import com.example.bacheloractivitytracker.repositories.SensorsDataRepositary;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.Objects;

import io.reactivex.disposables.Disposable;
import lombok.Getter;


public class ConnectedDevice {
    private static final String TAG = "ConnectedDevice";

    @Getter
    private String serial;
    @Getter
    private int steps;
    @Getter
    private int distance;
    @Getter
    private float heartRate;
    @Getter
    private int calories;
    @Getter
    private ConnectedDeviceModel connectedDeviceModel;

    private DashboardRecyclerAdapter adapter;
    private Disposable mHrSubscription;

    public ConnectedDevice(int steps, int distance, int heartRate, int calories, ConnectedDeviceModel connectedDeviceModel, DashboardRecyclerAdapter adapter) {
        this.steps = steps;
        this.distance = distance;
        this.heartRate = heartRate;
        this.calories = calories;
        this.connectedDeviceModel = connectedDeviceModel;
        this.serial = connectedDeviceModel.getBody().getSerial();
        this.adapter = adapter;

        initHR();
    }

    private void initHR() {
        mHrSubscription = SensorsDataRepositary.getInstance().subscribeToHeartRate(serial).subscribe(s -> {
            heartRate = RxMds.Instance.getGson().fromJson(s, HeartRateModel.class).getBody().getAverage();
            Log.d(TAG, "initHR: " + s);
            adapter.notifyDataSetChanged();

        }, throwable -> {
            Log.d(TAG, "accept: " + throwable);
            if(mHrSubscription != null) {
                stopSubsHeartRate();
            }

        });
    }

    public void stopSubsHeartRate() {
        if (mHrSubscription != null) {
            mHrSubscription.dispose();
        }
    }







    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ConnectedDevice) {
            ConnectedDevice that = (ConnectedDevice) o;
            return this.getSerial().equals(that.getSerial());
        } else if (o instanceof ConnectedDeviceModel) {
            ConnectedDeviceModel that = (ConnectedDeviceModel) o;
            return this.getSerial().equals(that.getBody().getSerial());

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serial, steps, distance, heartRate, calories, connectedDeviceModel);
    }
}
