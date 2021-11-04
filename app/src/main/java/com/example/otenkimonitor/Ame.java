package com.example.otenkimonitor;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Ame {
    private float Battery;
    private float Humidity;
    private float Temp;
    private Timestamp date;
    public Ame(){}

    public float getBattery() {
        return Battery;
    }

    public float getHumidity() {
        return Humidity;
    }

    public float getTemp() {
        return Temp;
    }

    public Date getDate() {
        return date.toDate();
    }
}
