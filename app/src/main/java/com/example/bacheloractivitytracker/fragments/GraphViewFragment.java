package com.example.bacheloractivitytracker.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.models.StepBundle;
import com.example.bacheloractivitytracker.viewModels.GraphViewFragmentViewModel;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;

public class GraphViewFragment extends Fragment {
    private static final String TAG = "GraphViewFragment";

    @BindView(R.id.linearAcc_lineChart)
    LineChart mChart;

    @BindView(R.id.x_axis_textView)
    TextView xAxis;

    @BindView(R.id.y_axis_textView)
    TextView yAxis;

    @BindView(R.id.z_axis_textView)
    TextView zAxis;

    private String serial;

    @BindView(R.id.steps_value)
    TextView stepView;

    @BindView(R.id.reset)
    Button reset;

    @BindView(R.id.action_state)
    TextView actionState;

    @BindView(R.id.distance_value)
    TextView distanceView;

    private GraphViewFragmentViewModel graphViewFragmentViewModel;
    private LiveData<StepBundle> mStepBundle;
    private Observer mStepObserver;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_view, container, false);
        ButterKnife.bind(this, view);

        initGraph();

        //TMP VECI - ORPAVIT

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                graphViewFragmentViewModel.reset();
            }
        });

        actionState.setText("unknown");

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GraphViewFragmentArgs args = GraphViewFragmentArgs.fromBundle(getArguments());
        serial = args.getSerial();
        Log.d(TAG, "onViewCreated: " + serial);
        initViewModel();

    }

    private void initViewModel() {
        graphViewFragmentViewModel = new ViewModelProvider(this).get(GraphViewFragmentViewModel.class);
        graphViewFragmentViewModel.init(serial);
    }

    private void initGraph() {
        mChart.setData(new LineData());
        mChart.getDescription().setText("Linear acceleration");
        mChart.setTouchEnabled(false);
        mChart.setAutoScaleMinMaxEnabled(true);
        mChart.invalidate();
    }

    @OnCheckedChanged(R.id.switchSubscription)
    public void onCheckedChange(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {

            if (serial.equals("none")) {
                Log.e(TAG, "onCheckedChange: serial number was not send");
                buttonView.setChecked(false);
                return;
            }

            LineData mLineData = initChart();

            mStepBundle = graphViewFragmentViewModel.getStepBundle();
            mStepObserver = (Observer<StepBundle>) stepBundle -> {
                String state = stepBundle.getState();
                float timestamp = stepBundle.getTimestamp();
                float magnitude = stepBundle.getMagnitude();
                int steps = stepBundle.getSteps();
                float distance = stepBundle.getDistance();

                stepView.setText(String.format(Locale.getDefault(), "steps: %d", steps));
                xAxis.setText(String.format(Locale.getDefault(),
                        "x: %.6f", magnitude));
                actionState.setText(state);
                distanceView.setText(String.format(Locale.getDefault(), "Distance: %.2f", distance));


                mLineData.addEntry(new Entry(timestamp / 100, magnitude), 0);
                mLineData.notifyDataChanged();


                // let the chart know it's data has changed
                mChart.notifyDataSetChanged();

                // limit the number of visible entries
                mChart.setVisibleXRangeMaximum(50);

                // move to the latest entry
                mChart.moveViewToX(timestamp / 100);
            };

            mStepBundle.observe(this, mStepObserver);
        } else {
            if (mStepBundle != null && mStepObserver != null) {
                mStepBundle.removeObserver(mStepObserver);
                graphViewFragmentViewModel.stopSubToSteps();
            }
        }
    }

    private LineData initChart() {
        LineData mLineData = mChart.getData();

        ILineDataSet xSet = mLineData.getDataSetByIndex(0);
        //ILineDataSet ySet = mLineData.getDataSetByIndex(1);
        //ILineDataSet zSet = mLineData.getDataSetByIndex(2);

        if (xSet == null) {
            xSet = createSet("Data x", getResources().getColor(android.R.color.holo_red_dark));
            //ySet = createSet("Data y", getResources().getColor(android.R.color.holo_green_dark));
            //zSet = createSet("Data z", getResources().getColor(android.R.color.holo_blue_dark));
            mLineData.addDataSet(xSet);
            //mLineData.addDataSet(ySet);
            //mLineData.addDataSet(zSet);
        }
        return mLineData;
    }

    private LineDataSet createSet(String name, int color) {
        LineDataSet set = new LineDataSet(null, name);
        set.setLineWidth(2.5f);
        set.setColor(color);
        set.setDrawCircleHole(false);
        set.setDrawCircles(false);
        set.setMode(LineDataSet.Mode.LINEAR);
        set.setHighLightColor(Color.rgb(190, 190, 190));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setValueTextSize(0f);

        return set;
    }
}

