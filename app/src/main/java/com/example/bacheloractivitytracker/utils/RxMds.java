package com.example.bacheloractivitytracker.utils;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.bacheloractivitytracker.models.ConnectedDeviceModel;
import com.example.bacheloractivitytracker.models.MdsSubscriptionURI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsConnectionListener;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsHeader;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsResponseListener;
import com.movesense.mds.MdsSubscription;
import java.util.Objects;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.Single;
import lombok.Getter;

public enum RxMds {
    Instance;

    private static final String TAG = "RxMds";

    private Mds mMds;
    private Context context;

    @Getter
    private Gson gson;


    public void init (Context context) {
        mMds = Mds.builder().build(context);
        this.context = context;
        gson = new GsonBuilder().create();
    }

    private Single<String> get(String serial, String uriPath) {
        return Single.create(singleSubscriber -> {

            String uri = Formatter.createGetUri(serial, uriPath);
            mMds.get(uri, null, new MdsResponseListener() {
                @Override
                public void onError(MdsException e) {
                    singleSubscriber.onError(e);
                }

                @Override
                public void onSuccess(String data, MdsHeader header) {
                    singleSubscriber.onSuccess(data);
                }
            });
        });
    }


    public Observable<String> subscribe(String serial, String uriPath, String rate) {
        Log.e(TAG, "subscribe: " + uriPath);
        return Observable.create((ObservableEmitter<String> emitter) -> {
            final MdsSubscription subscription = mMds.subscribe(Path.URI_EVENTLISTENER, gson.toJson(new MdsSubscriptionURI(serial, uriPath, rate)),
                    new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            Log.d(TAG, "onNotification: called" + s);
                            emitter.onNext(s);
                        }

                        @Override
                        public void onError(MdsException e) {
                            Log.d(TAG, "onError: " + e);
                            emitter.onError(e);
                        }
                    });
            emitter.setCancellable(subscription::unsubscribe);
        });
    }

    //used for the connectedDevices
    public Observable<String> genericSubscribe(String uri) {
        Log.e(TAG, "subscribe: " + uri);
        return Observable.create((ObservableEmitter<String> emitter) -> {
            final MdsSubscription subscription = mMds.subscribe(Path.URI_EVENTLISTENER, gson.toJson(new MdsSubscriptionURI(uri)),
                    new MdsNotificationListener() {
                        @Override
                        public void onNotification(String s) {
                            emitter.onNext(s);
                        }

                        @Override
                        public void onError(MdsException e) {
                            emitter.onError(e);
                        }
                    });

            emitter.setCancellable(subscription::unsubscribe);
        });
    }

    public Observable<ConnectedDeviceModel> connectedDeviceObservable() {
        return genericSubscribe(Path.URI_CONNECTED_DEVICES)
                .map(s -> {
                    Log.e(TAG, "connectedDeviceObservable(): " + s );
                    return gson.fromJson(s, ConnectedDeviceModel.class);
                })
                .filter(Objects::nonNull);
    }

    public void connect(String macAddress, @Nullable MdsConnectionListener mdsConnectionListener) {
        if(macAddress == null) {
            Log.e(TAG, "connect: macAddress is null");
            return;
        }
        mMds.connect(macAddress, mdsConnectionListener);
    }


    ///API FOR ACC, GYRO, MAGNOTOMETER
    public Observable<String> subscribeToHeartRate(String serial) {
        return subscribe(serial, Path.URI_HR_PATH_SUB, "");
    }

    public Observable<String> subscribeToECG(String serial, String rate) {
        return subscribe(serial, Path.URI_ECG_PATH_SUB, rate);
    }

    public Observable<String> subscribeToAcc(String serial, String rate) {
        return subscribe(serial, Path.URI_ACC_PATH_SUB, rate);
    }

    public Observable<String> subscribeToGyro(String serial, String rate) {
        return subscribe(serial, Path.URI_GYRO_PATH_SUB, rate);
    }











}
