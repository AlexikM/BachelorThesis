package com.example.bacheloractivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.repositories.ConnectedDevicesRepository;
import com.example.bacheloractivitytracker.repositories.SensorsDataRepositary;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.List;


//TODO https://www.youtube.com/watch?v=H9D_HoOeKWM
public class MainActivity extends AppCompatActivity {

    private ConnectedDevicesRepository mConnectedDevicesRepo;
    private SensorsDataRepositary mSensorsDataRepo;
    private LiveData<List<ConnectedDeviceModel>> connectedDevices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize MDS lib
        RxMds.Instance.init(getApplicationContext());

        //repo which handle connected devices
        mConnectedDevicesRepo = ConnectedDevicesRepository.getInstance();
        connectedDevices = mConnectedDevicesRepo.getConnectedDevices();
    }

    public LiveData<List<ConnectedDeviceModel>> getConnectedDevices() {
        return connectedDevices;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
