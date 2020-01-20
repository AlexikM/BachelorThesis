package com.example.bacheloractivitytracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;

import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.repositories.ConnectedDevicesRepository;
import com.example.bacheloractivitytracker.utils.RxBle;
import com.example.bacheloractivitytracker.utils.RxMds;

import java.util.List;

import io.reactivex.functions.Consumer;

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

    public LiveData<List<ConnectedDevice>> getConnectedDevices() {
        return mRepo.getConnnectedDevices();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mRepo.stop();
    }
}
