package com.example.bacheloractivitytracker.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;
import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.viewModels.DashboardFragmentViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardRecyclerAdapter extends RecyclerView.Adapter<DashboardRecyclerAdapter.DashboardViewHolder> {
    private static final String TAG = "DashboardRecyclerAdapter";


    private List<ConnectedDevice> connectedDevices;
    private View.OnClickListener onClickListener;

    public DashboardRecyclerAdapter(LiveData<List<ConnectedDeviceModel>> changedConnectedDevices, LifecycleOwner owner, View.OnClickListener onClickListener) {
        connectedDevices = new ArrayList<>();
        changedConnectedDevices.observe(owner, this::handleConnectivity);
        this.onClickListener = onClickListener;

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
        ConnectedDevice device = connectedDevices.get(position);

        holder.itemView.setTag(device);
        //TODO OPRAVIT TU
        holder.heartRate.setText(String.format(Locale.getDefault(), "%.0f", device.getHeartRate()));
        holder.calories.setText(Integer.toString(device.getCalories()));
        //holder.heartRate.setText(Float.toString(device.getHeartRate()));
        holder.distance.setText(Integer.toString(device.getDistance()));
        holder.steps.setText(Integer.toString(device.getSteps()));

        holder.itemView.setOnClickListener(onClickListener);
    }

    @Override
    public int getItemCount() {
        return connectedDevices.size();
    }




    //----------CONNECTIVETY -------------//
    private void handleConnectivity(List<ConnectedDeviceModel> connectedDeviceModels) {
        int sizeModels = connectedDeviceModels.size();
        int connectedSize = connectedDevices.size();

        if (sizeModels > connectedSize) {
            //pripojeno
            handleConnection(connectedDeviceModels);
        } else if (sizeModels < connectedSize) {
            //odpojeno
            handleDisconnection(connectedDeviceModels);
        }
    }

    private void handleConnection(List<ConnectedDeviceModel> connectedDeviceModels) {
        //called when the fragment is created or connected one device
        if(connectedDevices.size() == 0) {
            for(ConnectedDeviceModel device : connectedDeviceModels) {
                connectedDevices.add(new ConnectedDevice(0, 0, 0, 0, device, this));
            }
            notifyDataSetChanged();
            return;
        }

        //for reconnection when the device is connected automatically
        for (ConnectedDeviceModel model : connectedDeviceModels) {
            for (ConnectedDevice device : connectedDevices) {
                if(!device.getSerial().equals(model.getBody().getSerial())) {
                    connectedDevices.add(new ConnectedDevice(0,0,0,0, model, this));
                    notifyDataSetChanged();
                    return;
                }
            }
        }
    }

    private void handleDisconnection(List<ConnectedDeviceModel> connectedDeviceModels) {
        ConnectedDeviceModel toBeRemove = null;
        for (ConnectedDeviceModel model : connectedDeviceModels) {
            if(!connectedDevices.contains(model)) {
                toBeRemove = model;
                break;
            }
        }

        if (toBeRemove != null) {
            removeModel(toBeRemove);
            notifyDataSetChanged();
        }

    }

    private void removeModel(ConnectedDeviceModel model) {
        if(model != null) {
            ConnectedDevice toBeRemove = null;
            for (ConnectedDevice device : connectedDevices) {
                if(device.equals(model)) {
                    toBeRemove = device;
                    break;
                }

            }
            if(toBeRemove != null) {
                connectedDevices.remove(toBeRemove);
            }
        }
    }

    public void unsubscribeAll() {
        for(ConnectedDevice device : connectedDevices) {
            device.stopSubsHeartRate();
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

        DashboardViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
