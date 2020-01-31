package com.example.bacheloractivitytracker.repositories;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;

public class ConnectedDevicesRepository {
    private static final String TAG = "ConnectedDevicesReposit";


    private static ConnectedDevicesRepository instance;
    private List<ConnectedDeviceModel> connectedDevices = new ArrayList<>();
    private MutableLiveData<List<ConnectedDeviceModel>> mDataSet = new MutableLiveData<>();

    private Disposable mConnectedDevicesSubscription;

    public static ConnectedDevicesRepository getInstance() {
        if (instance == null) {
            instance = new ConnectedDevicesRepository();
        }

        return instance;
    }

    private void startHandlingConnectedDevices() {
        mConnectedDevicesSubscription = RxMds.Instance.connectedDeviceObservable().subscribe(connectedDevice -> {
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


    private void addDevice(ConnectedDeviceModel connectedDevice) {
        connectedDevices.add(connectedDevice);
        mDataSet.setValue(connectedDevices);
    }

    private void removeDevice(ConnectedDeviceModel connectedDevice) {
        if(connectedDevices.contains(connectedDevice)) {
            connectedDevices.remove(connectedDevice);

            mDataSet.setValue(connectedDevices);
        }
    }

    private void stopHandlingConnectedDevices() {
        mConnectedDevicesSubscription.dispose();
        mConnectedDevicesSubscription = null;
    }

    public LiveData<List<ConnectedDeviceModel>> getConnectedDevices() {
        if(mConnectedDevicesSubscription == null) {
            startHandlingConnectedDevices();
        }
        return mDataSet;
    }

    @Override
    protected void finalize() throws Throwable {
        stopHandlingConnectedDevices();
        super.finalize();
    }
}
