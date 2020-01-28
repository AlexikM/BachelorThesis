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

import com.example.bacheloractivitytracker.R;
import com.example.bacheloractivitytracker.models.Vector3D;
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
    private final Double DELTATIME = 0.076923076923077;

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

    //test
    private long streakStartTime;
    private long streakPrevTime;
    private static final int ABOVE = 1;
    private static final int BELOW = 0;
    private static int CURRENT_STATE = BELOW;
    private static int PREVIOUS_STATE = BELOW;
    private int stepCount = 0;

    @BindView(R.id.steps_value)
    TextView stepView;

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


            mSubscribeAcc = SensorsDataRepositary.getInstance().subscribeToAcc(serial, "26").subscribe(s -> {
                Log.d(TAG, "onCheckedChange: " + s);

                LinearAccelerationModel result = RxMds.Instance.getGson().fromJson(s, LinearAccelerationModel.class);

                //TODO IF jestli result neni null

//                ArrayModel arrayModel = result.getBody().getArray()[0];
////                ArrayModel arrayModel1 = result.getBody().getArray()[1];

                float[] avgResult = avgResult(result);


                float timestamp = result.getBody().getTimestamp();

                prev = lowPassFilter(avgResult, prev);

                handleStepDetection(prev);
//
//                float x = (float) arrayModel.getX() - prev[0];
//                float y = (float) arrayModel.getY() - prev[1];
//                float z = (float) arrayModel.getZ() - prev[2];
//                float r = (float) Math.sqrt(x*x + y*y + z*z);


//                xAxis.setText(String.format(Locale.getDefault(),
//                        "x: %.6f", arrayModel.getX()));
//                yAxis.setText(String.format(Locale.getDefault(),
//                        "y: %.6f", arrayModel.getY()));
//                zAxis.setText(String.format(Locale.getDefault(),
//                        "z: %.6f", arrayModel.getZ()));

//                xAxis.setText(String.format(Locale.getDefault(),
//                        "x: %.6f", x));
//                yAxis.setText(String.format(Locale.getDefault(),
//                        "y: %.6f", y));
//                zAxis.setText(String.format(Locale.getDefault(),
//                        "z: %.6f", z));


                xAxis.setText(String.format(Locale.getDefault(),
                        "x: %.6f", prev[0]));
                yAxis.setText(String.format(Locale.getDefault(),
                        "y: %.6f", prev[1]));
                zAxis.setText(String.format(Locale.getDefault(),
                        "z: %.6f", prev[2]));

//                mLineData.addEntry(new Entry(timestamp / 100, x), 0);
//                mLineData.addEntry(new Entry(timestamp / 100, y), 1);
//                mLineData.addEntry(new Entry(timestamp / 100, z), 2);

//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getX()), 0);
//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getY()), 1);
//                mLineData.addEntry(new Entry(timestamp / 100, (float) arrayModel.getZ()), 2);
//
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

    //TODO mozna later on zkusit znovu pocitat rychlost?  s timhle by to mozna nejak slo idk
    private float[] lowPassFilter(float[] input, float[] prev) {
        float ALPHA = 0.5f;
        if (input == null || prev == null) {
            return null;
        }
        for (int i = 0; i < input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }

//    private void updateVelocity(Vector3D acceleration) {
//        acceleration.multiplyByScalar(DELTATIME);
//        v0.add(acceleration);
//    }

    private float[] avgResult(LinearAccelerationModel accelerationModel) {
        ArrayModel model1 = accelerationModel.getBody().getArray()[0];
        ArrayModel model2 = accelerationModel.getBody().getArray()[1];

        float x = (float) ((model1.getX() + model2.getX()) / 2);
        float y = (float) ((model1.getY() + model2.getY()) / 2);
        float z = (float) ((model1.getZ() + model2.getZ()) / 2);

        float[] result = {x, y, z};
        return result;
    }

    private void handleStepDetection(float[] data) {
        float y = data[1];

        if (y > 12f) {
            CURRENT_STATE = ABOVE;
            if (PREVIOUS_STATE != CURRENT_STATE) {
                streakStartTime = System.currentTimeMillis();
                if ((streakStartTime - streakPrevTime) <= 250f) {
                    streakPrevTime = System.currentTimeMillis();
                    return;
                }
                streakPrevTime = streakStartTime;
                Log.d("STATES:", "" + streakPrevTime + " " + streakStartTime);
                stepCount++;
            }
            PREVIOUS_STATE = CURRENT_STATE;
        } else if (y < 12f) {
            CURRENT_STATE = BELOW;
            PREVIOUS_STATE = CURRENT_STATE;
        }
        stepView.setText("steps: " + (stepCount));

    }


}

