package com.example.bacheloractivitytracker.utils;

import com.example.bacheloractivitytracker.rawDataModels.ArrayModel;
import com.example.bacheloractivitytracker.rawDataModels.LinearAccelerationModel;

public class DataSmoothing {

    //used when the subscription rate is 26
    public static float[] avgData(LinearAccelerationModel accelerationModel) {
        ArrayModel model1 = accelerationModel.getBody().getArray()[0];
        ArrayModel model2 = accelerationModel.getBody().getArray()[1];

        float x = (float) ((model1.getX() + model2.getX()) / 2);
        float y = (float) ((model1.getY() + model2.getY()) / 2);
        float z = (float) ((model1.getZ() + model2.getZ()) / 2);

        return new float[]{x, y, z};
    }

    public static float[] lowPassFilter(float[] input, float[] prev) {
        float ALPHA = 0.6f;
        if (input == null || prev == null) {
            return null;
        }
        for (int i = 0; i < input.length; i++) {
            prev[i] = prev[i] + ALPHA * (input[i] - prev[i]);
        }
        return prev;
    }
}
