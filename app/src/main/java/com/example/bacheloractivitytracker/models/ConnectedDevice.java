package com.example.bacheloractivitytracker.models;


import lombok.Getter;

@Getter
public class ConnectedDevice {

    private String serial;
    private int steps;
    private int distance;
    private int heartRate;
    private ConnectedDeviceModel connectedDeviceModel;

    public ConnectedDevice(int steps, int distance, int heartRate, ConnectedDeviceModel connectedDeviceModel) {
        this.steps = steps;
        this.distance = distance;
        this.heartRate = heartRate;
        this.connectedDeviceModel = connectedDeviceModel;
        this.serial = connectedDeviceModel.getBody().getSerial();
    }
}
