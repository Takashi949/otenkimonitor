package com.example.otenkimonitor;

import com.google.firebase.Timestamp;

import java.util.Date;

public class Fan {
    Timestamp date;
    Integer fan1;
    Integer fan2;
    public Fan(){}

    public Date getDate() {
        return date.toDate();
    }

    public Integer getFan1() {
        return fan1;
    }

    public Integer getFan2() {
        return fan2;
    }
}
