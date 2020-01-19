package com.example.bacheloractivitytracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bacheloractivitytracker.models.DeviceWrapper;
import com.example.bacheloractivitytracker.repositories.ConnectionRepository;

import java.util.List;

public class ConnectionFragmentViewModel extends ViewModel {

    private MutableLiveData<List<DeviceWrapper>> mScannedDevices;
    private ConnectionRepository mRepo;

    public void init() {
        if(mScannedDevices != null) {
            return;
        }

        mRepo = ConnectionRepository.getInstance();
        mScannedDevices = mRepo.getDataSet();
    }

    public LiveData<List<DeviceWrapper>> getScannedDevices() {
        return mScannedDevices;
    }

    public void startScan() {
        mRepo.startScanning();
    }


}
