package com.example.bacheloractivitytracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bacheloractivitytracker.MainActivity;
import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.adapters.DashboardRecyclerAdapter;
import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;

import java.util.List;

import butterknife.BindView;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.dashboard_recycler)
    RecyclerView mRecyclerView;

    private DashboardRecyclerAdapter mAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        initRecyclerView();

        return view;

    }

    private void initRecyclerView() {
        LiveData<List<ConnectedDeviceModel>> dataSet = ((MainActivity) getActivity()).getConnectedDevices();
        mAdapter = new DashboardRecyclerAdapter(dataSet, this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }
}
