package com.example.otenkimonitor.ui.Graph;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;

public class DateAxisFomatter extends ValueFormatter {
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    @Override
    public String getFormattedValue(float value) {
        return sdf.format(value);
    }
}
