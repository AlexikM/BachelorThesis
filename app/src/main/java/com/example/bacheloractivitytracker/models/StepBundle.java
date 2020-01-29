package com.example.bacheloractivitytracker.models;


import lombok.Getter;

//pomocna trida na LiveData. Nese vsechny potrebene informace o krocich do view casti
@Getter
public class StepBundle {

    private int steps;
    private String state;
    private float magnitude;
    private float timestamp;
    private float distance;

    public StepBundle(int steps, String state, float magnitude, float timestamp, float distance) {
        this.steps = steps;
        this.state = state;
        this.magnitude = magnitude;
        this.timestamp = timestamp;
        this.distance = distance;
    }
}
