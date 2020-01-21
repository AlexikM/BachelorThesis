package com.example.bacheloractivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.repositories.ConnectedDevicesRepository;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ConnectedDevicesRepository mRepo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize MDS lib
        RxMds.Instance.init(getApplicationContext());

        //repo which handle connected devices
        mRepo = ConnectedDevicesRepository.getInstance();
        mRepo.start();
    }

    public LiveData<List<ConnectedDeviceModel>> getConnectedDevices() {
        return mRepo.getConnnectedDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRepo.stop();
    }
}
