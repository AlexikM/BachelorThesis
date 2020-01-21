package com.example.bacheloractivitytracker.repositories;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.utils.RxMds;

import io.reactivex.disposables.Disposable;

public class ConnectedDevicesRepository {
    private static final String TAG = "ConnectedDevicesReposit";


    private static ConnectedDevicesRepository instance;
//    private List<ConnectedDeviceModel> connectedDevices = new ArrayList<>();
//    private MutableLiveData<List<ConnectedDeviceModel>> mDataSet = new MutableLiveData<>();

    private MutableLiveData<ConnectedDeviceModel> mMutableDevice = new MutableLiveData<>();
    private Disposable mSubscription;

    public static ConnectedDevicesRepository getInstance() {
        if (instance == null) {
            instance = new ConnectedDevicesRepository();
        }

        return instance;
    }

//    public void start() {
//        mSubscription = RxMds.Instance.connectedDeviceObservable().subscribe(connectedDevice -> {
//            if(connectedDevice.getBody().getConnection() == null) {
//                //disconnected coz of range
//                removeDevice(connectedDevice);
//                Log.d(TAG, "accept: ODEBRANO. Stav sezname je :" + connectedDevices.size());
//            } else {
//                //pripojeno
//                addDevice(connectedDevice);
//                Log.d(TAG, "accept: PRIDANO. Stav sezname je :" + connectedDevices.size());
//            }
//        });
//    }

    public void start() {
        mSubscription = RxMds.Instance.connectedDeviceObservable().subscribe(connectedDevice -> {
            mMutableDevice.setValue(connectedDevice);
        });
    }

//    private void addDevice(ConnectedDeviceModel connectedDevice) {
//        connectedDevices.add(connectedDevice);
//        mDataSet.setValue(connectedDevices);
//    }
//
//    private void removeDevice(ConnectedDeviceModel connectedDevice) {
//        if(connectedDevices.contains(connectedDevice)) {
//            connectedDevices.remove(connectedDevice);
//
//            mDataSet.setValue(connectedDevices);
//        }
//    }

    public void stop() {
        mSubscription.dispose();
    }

    public LiveData<ConnectedDeviceModel> getChangedDevice() {
        return mMutableDevice;
    }




}
