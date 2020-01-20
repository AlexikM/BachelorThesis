package com.example.bacheloractivitytracker.adapters;

import android.content.res.Resources;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.models.DeviceWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionRecyclerAdapter extends RecyclerView.Adapter<ConnectionRecyclerAdapter.ConnectionViewHolder>{
    private static final String TAG = "ConnectionRecAdapter";


    private List<DeviceWrapper> mBleScanResultList = new ArrayList<>();


    @NonNull
    @Override
    public ConnectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_scan_devices_list_item, parent, false);
        ConnectionViewHolder holder = new ConnectionViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectionViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        DeviceWrapper device = mBleScanResultList.get(position);

        int rssi = device.getRssi();

        StringBuilder sb = new StringBuilder();
        if (rssi > -50) {
            sb.append("Excellent");
        } else if (rssi <= -50 && rssi >= -60) {
            sb.append("Good");
        } else if (rssi < -60 && rssi>= -70) {
            sb.append("Fair");
        } else {
            sb.append("Weak");
        }

        sb.append("(").append(rssi).append(")");

        holder.macAddress.setText(device.getMRxBleDevice().getMacAddress());
        holder.rssiStatus.setText(sb.toString());
    }

    @Override
    public int getItemCount() {
        return mBleScanResultList.size();
    }

    public void setDevicesScanned(List<DeviceWrapper> list) {
        this.mBleScanResultList = list;
        this.notifyDataSetChanged();
    }

    class ConnectionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.macAddress)
        TextView macAddress;

        @BindView(R.id.rssiStatus)
        TextView rssiStatus;

        ConnectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
