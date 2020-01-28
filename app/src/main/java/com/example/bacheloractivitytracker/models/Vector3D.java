package com.example.bacheloractivitytracker.models;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class Vector3D {

    private double x;
    private double y;
    private double z;
    private int counter = 0;
    private double avgMagnitude = 0;
    private List<Double> tmpValues = new ArrayList<>();

    public Vector3D(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void add(Vector3D vector) {
        this.x += vector.x;
        this.y += vector.y;
        this.z += vector.z;
    }

    public void multiplyByScalar(double scalar) {
        this.x *= scalar;
        this.y *= scalar;
        this.z *= scalar;
    }

    public double getMagnitute() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
//        if (counter == 10) {
//            double avgVelocity = 0;
//            for (Double value : tmpValues) {
//                avgVelocity += value;
//            }
//            avgVelocity = avgVelocity / tmpValues.size();
//            tmpValues = new ArrayList<>();
//            counter = 0;
//            avgMagnitude = avgVelocity;
//            return avgMagnitude;
//
//        } else {
//            double currentVel = Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
//            tmpValues.add(currentVel);
//            counter++;
//            return avgMagnitude;
//        }
    }
}
