package com.example.bacheloractivitytracker;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.bacheloractivitytracker.utils.RxBle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBle.Instance.initialize(getApplicationContext());
        setContentView(R.layout.activity_main);
    }
}
