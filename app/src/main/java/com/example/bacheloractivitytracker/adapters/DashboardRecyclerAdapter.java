package com.example.bacheloractivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder>{
    private static final String TAG = "DashboardRecyclerAdapter";


    private List<ConnectedDevice> connectedDevices;

    //LiveData<ConnectedDeviceModel> device is here the changing device which is being subscribe in
    //connectedDevicesRepo
    public DashboardRecyclerAdapter(LiveData<ConnectedDeviceModel> device, LifecycleOwner owner) {
        connectedDevices = new ArrayList<>();

        device.observe(owner, new Observer<ConnectedDeviceModel>() {
            @Override
            public void onChanged(ConnectedDeviceModel device) {
                if (device.getBody().getConnection() != null) {
                    //nove pripojeni
                    ConnectedDevice newDevice = new ConnectedDevice(0 ,0, 0, device);
                    connectedDevices.add(newDevice);
                } else {
                    //odpojeni
                    handleDisconnect(device.getBody().getSerial());
                }
            }
        });
    }


    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dashboard_list_item, parent, false);
        DashboardViewHolder holder = new DashboardViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    private void handleDisconnect(String serial) {
        ConnectedDevice toBeRemove = null;
        for (ConnectedDevice device : connectedDevices) {
            if (serial.equals(device.getSerial())) {
                toBeRemove = device;
                break;
            }
        }

        if(toBeRemove != null) {
            connectedDevices.remove(toBeRemove);
        }
    }



    class DashboardViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.steps_value)
        TextView steps;

        @BindView(R.id.distance_value)
        TextView distance;

        @BindView(R.id.calories_value)
        TextView calories;

        @BindView(R.id.HR_value)
        TextView heartRate;

        public DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
