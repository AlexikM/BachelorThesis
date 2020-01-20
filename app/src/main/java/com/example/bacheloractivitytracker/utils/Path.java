package com.example.bacheloractivitytracker.utils;

public final class Path {

    public static final String URI_SCHEME_PREFIX = "suunto://";
    public static final String URI_EVENTLISTENER = "suunto://MDS/EventListener";
    public static final String URI_CONNECTED_DEVICES = "suunto://MDS/ConnectedDevices";

    public static final String URI_ACC_PATH_SUB = "Meas/Acc/";
    public static final String URI_ACC_PATH_INFO = "/Meas/Acc/Info";
    public static final String URI_ECG_PATH_SUB = "Meas/ECG/";
    public static final String URI_TEMPERATURE_PATH_SUB = "Meas/Temp";
    public static final String URI_TEMPERATURE_PATH_GET = "/Meas/Temp";
    public static final String URI_GYRO_PATH_SUB = "Meas/Gyro/";
    public static final String URI_GYRO_PATH_INFO = "/Meas/Gyro/Info";
    public static final String URI_MAGN_PATH_SUB = "Meas/Magn/";
    public static final String URI_MAGN_PATH_INFO = "/Meas/Magn/Info";
    public static final String URI_HR_PATH_SUB = "Meas/Hr";
    public static final String URI_BATTERY_PATH_GET = "/System/Energy/Level";
    public static final String URI_BATTERY_PATH_SUB = "System/Energy/Level";
    public static final String URI_IMU9_PATH_SUB  = "Meas/IMU9/";

    //TIME?
    public static final String TIME_GET_PATH = "/Time";
}
