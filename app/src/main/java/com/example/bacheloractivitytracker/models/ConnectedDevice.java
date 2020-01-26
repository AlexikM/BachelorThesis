package com.example.bacheloractivitytracker.models;


import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o instanceof ConnectedDevice) {
            ConnectedDevice that = (ConnectedDevice) o;
            return this.getSerial().equals(that.getSerial());
        } else if (o instanceof ConnectedDeviceModel) {
            ConnectedDeviceModel that = (ConnectedDeviceModel) o;
            return this.getSerial().equals(that.getBody().getSerial());

        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(serial, steps, distance, heartRate, calories, connectedDeviceModel);
    }
}
