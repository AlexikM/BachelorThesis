package com.example.bacheloractivitytracker.models;


import lombok.Getter;

//pomocna trida na LiveData. Nese vsechny potrebene informace o krocich do view casti
@Getter
public class StepBundle {

    private int steps;
    private String state;
    private float magnitute;
    private float timestamp;

    public StepBundle(int steps, String state, float magnitute, float timestamp) {
        this.steps = steps;
        this.state = state;
        this.magnitute = magnitute;
        this.timestamp = timestamp;
    }
}
