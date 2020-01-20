package com.example.bacheloractivitytracker.utils;

import android.content.Context;
import android.util.Log;

import com.example.bacheloractivitytracker.models.ConnectedDevice;
import com.example.bacheloractivitytracker.models.MdsSubscriptionURI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.movesense.mds.Mds;
import com.movesense.mds.MdsException;
import com.movesense.mds.MdsNotificationListener;
import com.movesense.mds.MdsSubscription;

import java.lang.reflect.Type;
import java.util.Objects;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.functions.Cancellable;
import io.reactivex.functions.Function;

public enum RxMds {
    Instance;

    private static final String TAG = "RxMds";

    //TODO JSON PRIDAT?
    private Mds mMds;
    private Context context;
    private Gson gson;


    public void init (Context context) {
        mMds = Mds.builder().build(context);
        this.context = context;
        gson = new GsonBuilder().create();
    }


    public Observable<String> subscribe(String serial, String uriPath, String rate) {
        Log.e(TAG, "subscribe: " + uriPath);
        return Observable.create((ObservableEmitter<String> emitter) -> {
            final MdsSubscription subscription = mMds.subscribe(Path.URI_EVENTLISTENER, gson.toJson(new MdsSubscriptionURI(serial, uriPath, rate)),
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

    public Observable<ConnectedDevice> connectedDeviceObservable() {
        return genericSubscribe(Path.URI_CONNECTED_DEVICES)
                .map(s -> {
                    Log.e(TAG, "connectedDeviceObservable(): " + s );
                    return gson.fromJson(s, ConnectedDevice.class);
                })
                .filter(Objects::nonNull);
    }







}
