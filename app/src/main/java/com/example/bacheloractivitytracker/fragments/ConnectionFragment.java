package com.example.bacheloractivitytracker.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.adapters.ConnectionRecyclerAdapter;
import com.example.bacheloractivitytracker.utils.RxBle;
import com.example.bacheloractivitytracker.viewModels.ConnectionFragmentViewModel;
import butterknife.BindView;
import butterknife.ButterKnife;


public class ConnectionFragment extends Fragment {
    private static final String TAG = "ConnectionFragment";
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 98;

    @BindView(R.id.found_devices)
    RecyclerView mRecyclerView;

    private ConnectionFragmentViewModel mConnectionFragmentViewModel;
    private ConnectionRecyclerAdapter mAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_connection, container, false);
        ButterKnife.bind(this, view);

        mConnectionFragmentViewModel = ViewModelProviders.of(this).get(ConnectionFragmentViewModel.class);
        mConnectionFragmentViewModel.init();
        RxBle.Instance.initialize(getContext());
        initRecyclerView();
        checkLocationPermissionIsGranted();

        //TODO vyresit s permission, pokud mi ho neda tak to spadne a poprve to taky spadne coz je unlucky
        //Start scanning
        mConnectionFragmentViewModel.startScan();
        return view;
    }


    private void initRecyclerView() {
        mAdapter = new ConnectionRecyclerAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);

        //observing the liveData and if something change i am updating the adapter
        mConnectionFragmentViewModel.getScannedDevices().observe(this, deviceWrappers -> mAdapter.setDevicesScanned(deviceWrappers));
    }


    private boolean checkLocationPermissionIsGranted() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(getContext())
                        .setTitle("Location permission")
                        .setMessage("This application needs location permission because it uses Bluetooth LE.")
                        .setPositiveButton("OK", (dialogInterface, i) -> {
                            //Prompt the user once explanation has been shown
                            ActivityCompat.requestPermissions(getActivity(),
                                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                    MY_PERMISSIONS_REQUEST_LOCATION);
                        })
                        .create()
                        .show();

            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy called");
        mConnectionFragmentViewModel.stopScan();

    }
}
