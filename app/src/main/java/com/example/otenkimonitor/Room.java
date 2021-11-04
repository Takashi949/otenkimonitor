package com.example.otenkimonitor;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Room {
    private float Humidity;
    private float Temp;
    private Timestamp date;

    public Room(){}

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
