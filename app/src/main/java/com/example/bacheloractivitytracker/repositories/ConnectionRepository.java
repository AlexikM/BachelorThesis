package com.example.bacheloractivitytracker.repositories;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.example.bacheloractivitytracker.models.DeviceWrapper;
import com.example.bacheloractivitytracker.utils.RxBle;
import com.polidea.rxandroidble2.scan.ScanSettings;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.Disposable;


/**
 * Singleton pattern
 */
public class ConnectionRepository {
    private static final String TAG = "ConnectionRepository";

    private static ConnectionRepository instance;
    private MutableLiveData<List<DeviceWrapper>> mDataSet = new MutableLiveData<>();
    private List<DeviceWrapper> mBleScanResultList = new ArrayList<>();
    private Disposable mBleScanSubscription;


    public static ConnectionRepository getInstance() {
        if (instance == null) {
            instance = new ConnectionRepository();
        }
        return instance;
    }

    public void startScanning() {
        mBleScanSubscription = RxBle.Instance.getClient().scanBleDevices(
                new ScanSettings.Builder()
                        // .setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY) // change if needed
                        // .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES) // change if needed
                        .build()
                // add filters if needed
        )
                .subscribe(
                        scanResult -> {
                            // Process scan result here.
                            DeviceWrapper deviceWrapper = new DeviceWrapper(scanResult.getRssi(), scanResult.getBleDevice());
                            if (deviceWrapper.getMRxBleDevice().getName() != null && deviceWrapper.getMRxBleDevice().getName().contains("Movesense")) {
                                handleBleDevice(deviceWrapper);
                            }
                        },
                        throwable -> {
                            // Handle an error here.
                            Log.e(TAG, "startScanning: ", throwable);
                            stopScanning();
                        }
                );
    }

    public void stopScanning() {
        if (mBleScanSubscription != null) {
            mBleScanSubscription.dispose();
        }
    }

    private void addNewBleDevice(DeviceWrapper deviceWrapper) {
        mBleScanResultList.add(deviceWrapper);
        mDataSet.setValue(mBleScanResultList);
    }

    private void updateBleDevice(DeviceWrapper deviceWrapper) {
        mBleScanResultList.set(mBleScanResultList.indexOf(deviceWrapper), deviceWrapper);
        mDataSet.setValue(mBleScanResultList);
    }

    private void handleBleDevice(DeviceWrapper deviceWrapper) {
        if (mBleScanResultList.contains(deviceWrapper)) {
            updateBleDevice(deviceWrapper);
        } else {
            addNewBleDevice(deviceWrapper);
        }
    }

    public MutableLiveData<List<DeviceWrapper>> getDataSet() {
        return mDataSet;
    }
}
