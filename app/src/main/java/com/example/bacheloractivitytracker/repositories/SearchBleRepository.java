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
public class SearchBleRepository {
    private static final String TAG = "SearchBleRepository";

    private static SearchBleRepository instance;
    private MutableLiveData<List<DeviceWrapper>> mDataSet = new MutableLiveData<>();
    private List<DeviceWrapper> mBleScanResultList = new ArrayList<>();
    private Disposable mBleScanSubscription;

    public static SearchBleRepository getInstance() {
        if (instance == null) {
            instance = new SearchBleRepository();
        }
        return instance;
    }


    //SEARCHING FOR THE DEVICES//
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
                            long currentMillis = System.currentTimeMillis() / 1000;
                            checkActiveDevices(currentMillis);
                            if (scanResult.getBleDevice() != null && scanResult.getBleDevice().getName() != null && scanResult.getBleDevice().getName().contains("Movesense")) {
                                DeviceWrapper deviceWrapper = new DeviceWrapper(scanResult.getRssi(), scanResult.getBleDevice(), System.currentTimeMillis() / 1000);
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

    private void checkActiveDevices(Long currentTimestamp) {
        DeviceWrapper toRemove = null;
        //in sec, how long the device can be non-updated
        int maxDelay = 2;
        for (DeviceWrapper device : mBleScanResultList) {
            if(currentTimestamp - device.getTimestamp() > maxDelay) {
                toRemove = device;
                break;
            }
        }

        if(toRemove != null) {
            mBleScanResultList.remove(toRemove);
            mDataSet.setValue(mBleScanResultList);
        }
    }

    public MutableLiveData<List<DeviceWrapper>> getDataSet() {
        return mDataSet;
    }

}
