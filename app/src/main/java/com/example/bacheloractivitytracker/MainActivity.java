package com.example.bacheloractivitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.utils.RxBle;
import com.example.bacheloractivitytracker.utils.RxMds;

import io.reactivex.functions.Consumer;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxMds.Instance.init(getApplicationContext());
        setContentView(R.layout.activity_main);
    }
}
