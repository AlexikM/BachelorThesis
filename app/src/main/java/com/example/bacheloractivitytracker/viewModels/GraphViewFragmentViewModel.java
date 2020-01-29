package com.example.bacheloractivitytracker.viewModels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.bacheloractivitytracker.models.StepBundle;
import com.example.bacheloractivitytracker.rawDataModels.LinearAccelerationModel;
import com.example.bacheloractivitytracker.repositories.SensorsDataRepositary;
import com.example.bacheloractivitytracker.utils.DataSmoothing;
import com.example.bacheloractivitytracker.utils.RxMds;

import io.reactivex.disposables.Disposable;

public class GraphViewFragmentViewModel extends ViewModel {
    private static final String TAG = "GraphViewFragViewModel";

    private final float[] CONSTANTS = {9.9f, 10.8f, 14.2f, 15f, 18f};
    private final String[] STATES = {"Standing", "Walking", "Slow jogging", "Jogging", "Fast run"};


//    private MutableLiveData<Integer> mStepCounter;
//    private MutableLiveData<String> mState;
//    private MutableLiveData<Float> mMagnitude;
//    private MutableLiveData<Float> mTimestamp;

    private MutableLiveData<StepBundle> mStepBundle;
    private int stepCount = 0;
    private float distance = 0;
    private float mMagnitude;
    private float mTimestamp;
    private String mState;


    private Disposable mAccelerationSubscription;
    private SensorsDataRepositary mRepo;
    private String serial;


    //steps
    private long streakStartTime;
    private long streakPrevTime;

    private Long standingStartTime;

    private static final int ABOVE = 1;
    private static final int BELOW = 0;
    private static int CURRENT_STATE = BELOW;
    private static int PREVIOUS_STATE = BELOW;
    private float sampleHistory = 0;
    private int counter = 0;
    private float[] prev = {0f, 0f, 0f};


    public void init(String serial) {
        if (mRepo == null) {
            mRepo = new SensorsDataRepositary();
        }
        this.serial = serial;
        this.mState = "unknown";
    }


    private void subscribeToSteps() {
        mAccelerationSubscription = SensorsDataRepositary.getInstance().subscribeToAcc(serial, "26").subscribe(s -> {
            LinearAccelerationModel result = RxMds.Instance.getGson().fromJson(s, LinearAccelerationModel.class);

            mTimestamp = ((float) result.getBody().getTimestamp());

            //smoothing the data
            float[] avgData = DataSmoothing.avgData(result);
            prev = DataSmoothing.lowPassFilter(avgData, prev);

            //magnitude
            float magnitude = (float) Math.sqrt(prev[0] * prev[0] + prev[1] * prev[1] + prev[2] * prev[2]);
            mMagnitude = (magnitude);
            handleStepDetection(magnitude);
            mStepBundle.setValue(new StepBundle(stepCount, mState, mMagnitude, mTimestamp, distance));

        }, throwable -> {
            Log.d(TAG, "accept: " + throwable);
            if (mAccelerationSubscription != null) {
                stopSubToSteps();
            }

        });
    }

    public void stopSubToSteps() {
        if (mAccelerationSubscription != null) {
            mAccelerationSubscription.dispose();
            mStepBundle = null;
        }
    }


    //9.9 standing
    //walking 10.8f
    //slow jogging 14.2
    //jogging 15
    //fast run 18
    //TODO zefektvnit tuhle funkci setText nemusi byt tak kde je, if if podminka muze byt predelana na if podminku
    private void handleStepDetection(float r) {
        if (r >= 10.8f) {
            handleActivityState(r);
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
                handleDistance();
                standingStartTime = null;
            }
            PREVIOUS_STATE = CURRENT_STATE;
        } else if (r < 10.8f) {
            if (standingStartTime == null) {
                standingStartTime = System.currentTimeMillis();
            } else if (System.currentTimeMillis() - standingStartTime > 4000f) {
                mState = (STATES[0]);
            }
            CURRENT_STATE = BELOW;
            PREVIOUS_STATE = CURRENT_STATE;
        }
    }


    private void handleActivityState(float r) {
        if (counter == 15) {
            float avg = sampleHistory / 15;
            int result = closestValue(avg);
            mState = (STATES[result]);

            counter = 0;
            sampleHistory = 0f;
        } else {
            sampleHistory += r;
            counter++;
        }
    }

    private int closestValue(float number) {
        float min = Math.abs(CONSTANTS[0] - number);
        int idx = 0;
        for (int c = 1; c < CONSTANTS.length; c++) {
            float newMin = Math.abs(CONSTANTS[c] - number);
            if (newMin < min) {
                idx = c;
                min = newMin;
            }
        }
        return idx;
    }

    public LiveData<StepBundle> getStepBundle() {
        if (mStepBundle == null) {
            mStepBundle = new MutableLiveData<>();
            subscribeToSteps();
        }
        return mStepBundle;
    }

    public void reset() {
        stepCount = 0;
    }

    private void handleDistance() {
        //for now hard coded 0.75 but later on we will judge by gender and weight and height
        distance += 0.75;

    }


}
