package com.example.bacheloractivitytracker.rawDataModels;

import com.google.gson.annotations.SerializedName;

import lombok.Getter;

@Getter
public class ArrayModel {
    @SerializedName("x")
    private final double x;

    @SerializedName("y")
    private final double y;

    @SerializedName("z")
    private final double z;

    public ArrayModel(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}