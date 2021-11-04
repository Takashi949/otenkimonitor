package com.example.otenkimonitor.ui.Command;

import android.util.Log;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.otenkimonitor.CmdResult;
import com.example.otenkimonitor.Fan;
import com.example.otenkimonitor.MainActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.HashMap;
import java.util.Map;

public class NotificationsViewModel extends ViewModel {
    private String TAG = "CMd";
    private MutableLiveData<Integer> fan1 = new MutableLiveData<>();
    private MutableLiveData<Integer> fan2 = new MutableLiveData<>();
    private MutableLiveData<String> msg = new MutableLiveData<>();

    public NotificationsViewModel() {
        fan1.setValue(0);
        fan2.setValue(0);
        MainActivity.firebaseFirestore.collection("CmdResult").document("cmdResult").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        CmdResult cmdResult = value.toObject(CmdResult.class);
                        if(!cmdResult.getMsg().equals("f"))msg.setValue(cmdResult.getMsg());
                    }
                }else {
                    Log.e(TAG, "onEvent: " + error.getMessage());
                }
            }
        });
        MainActivity.firebaseFirestore.collection("Fan").document("fan").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (value.exists()){
                        Fan fan = value.toObject(Fan.class);
                        fan1.setValue(fan.getFan1());
                        fan2.setValue(fan.getFan2());
                    }
                }else {
                    Log.e(TAG, "onEvent: " + error.getMessage());
                }
            }
        });
    }

    public void sendCmd(String cmd){
        Map<String, String> map = new HashMap<>();
        map.put("cmd", cmd);
        MainActivity.firebaseFirestore.collection("Cmd").document("cmd").set(map);
    }

    public LiveData<Integer> getFan1() {
        return fan1;
    }

    public LiveData<Integer> getFan2() {
        return fan2;
    }
    public LiveData<String> getMsg(){
        return msg;
    }
}