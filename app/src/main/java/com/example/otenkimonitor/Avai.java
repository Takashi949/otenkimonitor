package com.example.otenkimonitor;

import android.content.Intent;

import com.google.firebase.Timestamp;

public class Avai {
    String BatteryStatus;
    Integer cpu_usage;
    Integer mem;
    Double osc;
    Double temp;
    Timestamp date;

    public Avai(){}
    public boolean isAllGreen(){
        return (this.mem == 0 && this.BatteryStatus.endsWith("0") && this.temp < 60.0);
    }
    public String getBatteryStatus() {
        String msg = "";
        switch (BatteryStatus){
            case "0x0":{
                msg = "正常";
                break;
            }
            case "0x50000": {
                msg = "過去に低電圧状態になったが現在は正常";
                break;
            }
            case "0x50005": {
                msg = "現在低電圧状態にある";
                break;
            }
            case "0x80000":{
                msg = "過去に熱によりクロックダウンした";
                break;
            }
            case "0x80008": {
                msg = "現在熱によりクロックダウンしている";
                break;
            }
        }
        return msg;
    }

    public Integer getCpu_usage() {
        return cpu_usage;
    }

    public Timestamp getDate() {
        return date;
    }

    public String getMem() {
        if (mem == 0){
            return "正常";
        }
        else{
            return "メモリエラー" + String.valueOf(mem);
        }
    }

    public Double getOsc() {
        return osc;
    }

    public Double getTemp() {
        return temp;
    }
}
