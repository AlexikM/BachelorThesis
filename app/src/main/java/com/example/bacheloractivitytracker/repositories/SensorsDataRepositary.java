package com.example.bacheloractivitytracker.repositories;

import com.example.bacheloractivitytracker.utils.RxMds;

import io.reactivex.Observable;

public class SensorsDataRepositary {
    private static final String TAG = "SensorsDataRepositary";

    private static SensorsDataRepositary instance;

    public static SensorsDataRepositary getInstance() {
        if (instance == null) {
            instance = new SensorsDataRepositary();
        }
        return instance;
    }

    public Observable<String> subscribeToHeartRate(String serial) {
        return RxMds.Instance.subscribeToHeartRate(serial);
    }

    public Observable<String> subscribeToAcc(String serial, String rate) {
        return RxMds.Instance.subscribeToAcc(serial, rate);
    }




//    private void subscribeHeartRate(String serial) {
//        mHrSubscription = (RxMds.Instance.subscribeToHeartRate(serial).subscribe(s -> {
//            Log.d(TAG, "subscribeHeartRate: " + s);
//            heartRate.setValue(RxMds.Instance.getGson().fromJson(s, HeartRateModel.class));
//        }, throwable -> {
//            Log.d(TAG, "subscribeHeartRate: " + throwable);
//            mHrSubscription.dispose();
//        }));
//    }
//
//    public void unsubscriteHeartRate() {
//        mHrSubscription.dispose();
//    }
//
//    public MutableLiveData<HeartRateModel> getHeartRate(String serial) {
//        if (mHrSubscription == null || mHrSubscription.isDisposed()) {
//            subscribeHeartRate(serial);
//       }
//        return heartRate;
//    }


}
