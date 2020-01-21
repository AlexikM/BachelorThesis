package com.example.bacheloractivitytracker.models;


import lombok.Getter;

@Getter
public class ConnectedDevice {

    private String serial;
    private int steps;
    private int distance;
    private int heartRate;
    private int calories;
    private ConnectedDeviceModel connectedDeviceModel;

    public ConnectedDevice(int steps, int distance, int heartRate, int calories, ConnectedDeviceModel connectedDeviceModel) {
        this.steps = steps;
        this.distance = distance;
        this.heartRate = heartRate;
        this.calories = calories;
        this.connectedDeviceModel = connectedDeviceModel;
        this.serial = connectedDeviceModel.getBody().getSerial();
    }
}
