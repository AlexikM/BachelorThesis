package com.example.bacheloractivitytracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bacheloractivitytracker.models.DeviceWrapper;
import com.example.bacheloractivitytracker.repositories.SearchBleRepository;

import java.util.List;

public class ConnectionFragmentViewModel extends ViewModel {

    //TODO prejmenovat DeviceWrapper na neco vic reasonable
    private MutableLiveData<List<DeviceWrapper>> mScannedDevices;
    private SearchBleRepository mRepo;

    public void init() {
        if(mScannedDevices != null) {
            return;
        }

        mRepo = SearchBleRepository.getInstance();
        mScannedDevices = mRepo.getDataSet();
    }

    public LiveData<List<DeviceWrapper>> getScannedDevices() {
        return mScannedDevices;
    }

    public void startScan() {
        mRepo.startScanning();
    }

    public void stopScan() {
        mRepo.stopScanning();
    }


}
