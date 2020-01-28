package com.example.bacheloractivitytracker.fragments;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.rawDataModels.AngularVelocityModel;
import com.example.bacheloractivitytracker.rawDataModels.ArrayModel;
import com.example.bacheloractivitytracker.rawDataModels.LinearAccelerationModel;
import com.example.bacheloractivitytracker.repositories.SensorsDataRepositary;
import com.example.bacheloractivitytracker.utils.RxMds;
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
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

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


    private Disposable mSubscribeAcc;
    private String serial;
    private float[] prev = {0f, 0f, 0f};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph_view, container, false);
        ButterKnife.bind(this, view);

        initGraph();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        GraphViewFragmentArgs args = GraphViewFragmentArgs.fromBundle(getArguments());
        serial = args.getSerial();
        Log.d(TAG, "onViewCreated: " + serial);
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
                return;
            }

            LineData mLineData = mChart.getData();

            ILineDataSet xSet = mLineData.getDataSetByIndex(0);
            ILineDataSet ySet = mLineData.getDataSetByIndex(1);
            ILineDataSet zSet = mLineData.getDataSetByIndex(2);

            if (xSet == null) {
                xSet = createSet("Data x", getResources().getColor(android.R.color.holo_red_dark));
                ySet = createSet("Data y", getResources().getColor(android.R.color.holo_green_dark));
                zSet = createSet("Data z", getResources().getColor(android.R.color.holo_blue_dark));
                mLineData.addDataSet(xSet);
                mLineData.addDataSet(ySet);
                mLineData.addDataSet(zSet);
            }


            mSubscribeAcc = SensorsDataRepositary.getInstance().subscribeToAcc(serial, "13").subscribe(s -> {
                Log.d(TAG, "onCheckedChange: " + s);

                LinearAccelerationModel result = RxMds.Instance.getGson().fromJson(s, LinearAccelerationModel.class);

                //TODO IF jestli result neni null


                ArrayModel arrayModel = result.getBody().getArray()[0];
                float timestamp = result.getBody().getTimestamp();
                Log.d(TAG, "onCheckedChange: " + arrayModel.getX());

                float[] tmp = {(float) arrayModel.getX(), (float) arrayModel.getY(), (float) arrayModel.getZ()};
                prev = lowPassFilter(tmp, prev);


//                xAxis.setText(String.format(Locale.getDefault(),
//                        "x: %.6f", arrayModel.getX()));
//                yAxis.setText(String.format(Locale.getDefault(),
//                        "y: %.6f", arrayModel.getY()));
//                zAxis.setText(String.format(Locale.getDefault(),
//                        "z: %.6f", arrayModel.getZ()));

                xAxis.setText(String.format(Locale.getDefault(),
                        "x: %.6f", prev[0]));
                yAxis.setText(String.format(Locale.getDefault(),
                        "y: %.6f", prev[1]));
                zAxis.setText(String.format(Locale.getDefault(),
                        "z: %.6f", prev[2]));

//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getX()), 0);
//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getY()), 1);
//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getZ()), 2);

                mLineData.addEntry(new Entry(timestamp / 100, prev[0]), 0);
                mLineData.addEntry(new Entry(timestamp / 100, prev[1]), 1);
                mLineData.addEntry(new Entry(timestamp / 100, prev[2]), 2);
                mLineData.notifyDataChanged();


                // let the chart know it's data has changed
                mChart.notifyDataSetChanged();

                // limit the number of visible entries
                mChart.setVisibleXRangeMaximum(50);

                // move to the latest entry
                mChart.moveViewToX(timestamp / 100);


            }, throwable -> {
                Log.d(TAG, "onCheckedChange: " + throwable);
                if (mSubscribeAcc != null) {
                    mSubscribeAcc.dispose();
                    buttonView.setChecked(false);
                }
            });


        } else {
            mSubscribeAcc.dispose();
        }
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

    //test
    private float[] lowPassFilter(float[] input, float[] prev) {
        float ALPHA = 0.1f;
        if (input == null || prev == null) {
            return null;
        }
        for (int i = 0; i < input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }
}
