package com.example.otenkimonitor.ui.home;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.otenkimonitor.Avai;
import com.example.otenkimonitor.Extra;
import com.example.otenkimonitor.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeViewModel extends ViewModel {
    private MutableLiveData<Avai> avai = new MutableLiveData<>();
    private MutableLiveData<Extra> extra = new MutableLiveData<>();

    public HomeViewModel() {
        MainActivity.firebaseFirestore.collection("Avai").document("avai").addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
            if (error == null){
                if (value.exists()){
                    Avai ddd = value.toObject(Avai.class);

                    Log.d("Avai", ddd.getDate().toString());
                    avai.setValue(ddd);
                }
            }else {
                Log.e("Avai", error.toString());
            }
            }
        });
        MainActivity.firebaseFirestore.collection("Extra").whereEqualTo("msg","boot").orderBy("date", Query.Direction.DESCENDING).limit(1).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error == null){
                    if (!value.isEmpty()){
                        DocumentSnapshot document = value.getDocuments().get(0);
                        Extra ddd = value.getDocuments().get(0).toObject(Extra.class);

                        Log.d("Extra", ddd.getDate().toString());
                        extra.setValue(ddd);
                    }
                }else {
                    Log.e("Extra", error.toString());
                }
            }
        });
    }

    public LiveData<Avai> getAvai() {
        return avai;
    }
    public LiveData<Extra> getExtra(){
        return extra;
    }
}