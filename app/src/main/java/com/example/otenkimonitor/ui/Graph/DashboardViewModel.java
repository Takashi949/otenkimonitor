package com.example.otenkimonitor.ui.Graph;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.otenkimonitor.Ame;
import com.example.otenkimonitor.MainActivity;
import com.example.otenkimonitor.Room;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DashboardViewModel extends ViewModel {
    private List<Ame> plots = new ArrayList<>();
    private MutableLiveData<List<Ame>> ame = new MutableLiveData<>();
    private MutableLiveData<List<Room>> room = new MutableLiveData<>();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
    private SimpleDateFormat slg = new SimpleDateFormat("MM/dd/HH/mm");
    private String TAG = "Ame";

    public DashboardViewModel() {
        LoadDate(Calendar.getInstance());
    }

    public void LoadDate(Calendar objDate){
        Calendar stc = (Calendar) objDate.clone();
        Calendar edc = (Calendar) objDate.clone();

        stc.set(Calendar.HOUR_OF_DAY, 0);
        stc.set(Calendar.MINUTE, 0);
        stc.set(Calendar.SECOND, 0);

        edc.add(Calendar.DATE, 1);
        edc.set(Calendar.HOUR_OF_DAY, 0);
        edc.set(Calendar.MINUTE, 0);
        edc.set(Calendar.SECOND, 0);
        Log.d(TAG, "ObjeDate =>" + sdf.format(objDate.getTime()) + ":  search at =>" + slg.format(stc.getTime()) + "(" + stc.getTimeInMillis() + ")+" + slg.format(edc.getTime()) + "(" + edc.getTimeInMillis() + ")");
        MainActivity.firebaseFirestore.collection("Ame")
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", stc.getTime())
                .whereLessThanOrEqualTo("date", edc.getTime())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    plots.clear();
                    for(QueryDocumentSnapshot d: task.getResult()){
                        plots.add(d.toObject(Ame.class));
                    }

                    Log.d(TAG, String.valueOf(plots.size()));
                    ame.setValue(plots);
                }else {
                    Log.e(TAG, task.getException().toString());
                }
            }
        });
        MainActivity.firebaseFirestore.collection("Room")
                .orderBy("date", Query.Direction.ASCENDING)
                .whereGreaterThanOrEqualTo("date", stc.getTime())
                .whereLessThanOrEqualTo("date", edc.getTime())
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    List<Room> rooms = new ArrayList<>();
                    for (QueryDocumentSnapshot d: task.getResult()){
                        rooms.add(d.toObject(Room.class));
                    }
                    room.setValue(rooms);
                }
                else  {
                    Log.e(TAG, task.getException().toString());
                }
            }
        });
    }

    public LiveData<List<Ame>> getAme() {
        return ame;
    }
    public LiveData<List<Room>> getRoom(){return room;}
}