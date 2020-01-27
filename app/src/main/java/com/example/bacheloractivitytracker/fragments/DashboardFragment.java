package com.example.bacheloractivitytracker.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bacheloractivitytracker.MainActivity;
import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.adapters.DashboardRecyclerAdapter;
import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
//import com.example.bacheloractivitytracker.repositories.SensorsDataRepositary;
import com.example.bacheloractivitytracker.viewModels.DashboardFragmentViewModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DashboardFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.dashboard_recycler)
    RecyclerView mRecyclerView;

    @BindView(R.id.addNewDevice)
    Button addNewDevice;

    private DashboardRecyclerAdapter mAdapter;
    private NavController navController;
    private DashboardFragmentViewModel mDashboardFragmentViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);

        initViewModel();
        initRecyclerView();
        initAddBtn();

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
    }


    private void initViewModel() {
        mDashboardFragmentViewModel = ViewModelProviders.of(this).get(DashboardFragmentViewModel.class);

    }

    private void initAddBtn() {
        addNewDevice.setOnClickListener(this);

    }

    private void initRecyclerView() {
        LiveData<List<ConnectedDeviceModel>> dataSet = ((MainActivity) getActivity()).getConnectedDevices();
        mAdapter = new DashboardRecyclerAdapter(dataSet, this, mDashboardFragmentViewModel);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {
        navController.navigate(R.id.action_dashboardFragment_to_connectionFragment);

    }
}
