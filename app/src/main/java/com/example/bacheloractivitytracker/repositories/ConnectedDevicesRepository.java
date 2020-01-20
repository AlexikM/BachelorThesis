package com.example.bacheloractivitytracker.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ConnectedDevicesRepository {
    private static final String TAG = "ConnectedDevicesReposit";


    private static ConnectedDevicesRepository instance;
    private List<ConnectedDevice> connectedDevices = new ArrayList<>();
    private MutableLiveData<List<ConnectedDevice>> mDataSet = new MutableLiveData<>();
    private Disposable mSubscription;

    public static ConnectedDevicesRepository getInstance() {
        if (instance == null) {
            instance = new ConnectedDevicesRepository();
        }

        return instance;
    }

    public void start() {
        mSubscription = RxMds.Instance.connectedDeviceObservable().subscribe(connectedDevice -> {
            if(connectedDevice.getBody().getConnection() == null) {
                //disconnected coz of range
                removeDevice(connectedDevice);
                Log.d(TAG, "accept: ODEBRANO. Stav sezname je :" + connectedDevices.size());
            } else {
                //pripojeno
                addDevice(connectedDevice);
                Log.d(TAG, "accept: PRIDANO. Stav sezname je :" + connectedDevices.size());
            }
        });
    }

    private void addDevice(ConnectedDevice connectedDevice) {
        connectedDevices.add(connectedDevice);
        mDataSet.setValue(connectedDevices);
    }

    private void removeDevice(ConnectedDevice connectedDevice) {
        if(connectedDevices.contains(connectedDevice)) {
            connectedDevices.remove(connectedDevice);

            mDataSet.setValue(connectedDevices);
        }
    }

    public void stop() {
        mSubscription.dispose();
    }

    public LiveData<List<ConnectedDevice>> getConnnectedDevices() {
        return mDataSet;
    }




}
