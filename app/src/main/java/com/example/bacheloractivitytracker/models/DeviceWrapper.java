package com.example.bacheloractivitytracker.models;

import androidx.annotation.Nullable;

import com.polidea.rxandroidble2.RxBleDevice;

import lombok.Getter;

@Getter
public class DeviceWrapper {

    private int rssi;
    private RxBleDevice mRxBleDevice;

    public DeviceWrapper(int rssi, RxBleDevice rxBleDevice) {
        this.rssi = rssi;
        mRxBleDevice = rxBleDevice;
    }

    @Override
    public int hashCode() {
        int result = mRxBleDevice.hashCode();
        result = 31 * result + (mRxBleDevice != null ? mRxBleDevice.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(@Nullable Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DeviceWrapper that = (DeviceWrapper) o;

        return mRxBleDevice != null ? mRxBleDevice.getMacAddress().equals(that.mRxBleDevice.getMacAddress()) : that.mRxBleDevice.getMacAddress() == null;
    }
}
