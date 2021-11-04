package com.example.otenkimonitor.ui.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.otenkimonitor.Avai;
import com.example.otenkimonitor.Extra;
import com.example.otenkimonitor.MainActivity;
import com.example.otenkimonitor.R;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnFailureListener;

import org.w3c.dom.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HomeFragment extends Fragment {
    private TextView lastText;
    private TextView MeasText;
    private TextView CpuText;
    private TextView MemText;
    private TextView OscText;
    private TextView TempText;
    private TextView BatteryText;
    SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日HH時mm分");

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        lastText = root.findViewById(R.id.textReboot);
        MeasText = root.findViewById(R.id.textMeasure);
        CpuText = root.findViewById(R.id.textCPUUsage);
        MemText = root.findViewById(R.id.textMem);
        OscText = root.findViewById(R.id.textOsc);
        TempText = root.findViewById(R.id.textTemp);
        BatteryText = root.findViewById(R.id.textBattery);

        MainActivity.title.setText(R.string.load);
        MainActivity.toolbar.setNavigationIcon(R.drawable.ic_autorenew_black_24dp);

        homeViewModel.getAvai().observe(getViewLifecycleOwner(), new Observer<Avai>() {
            @Override
            public void onChanged(Avai avai) {
                Log.d("Avai", "change called" + avai.getDate().toString());

                Date date = avai.getDate().toDate();
                MeasText.setText(df.format(date));
                CpuText.setText(String.valueOf(avai.getCpu_usage()/1000000) + "MHz");
                MemText.setText(avai.getMem());
                OscText.setText(avai.getOsc().toString() + "MHz");
                TempText.setText(avai.getTemp().toString() + "℃");
                BatteryText.setText(avai.getBatteryStatus());

                if(avai.isAllGreen()){
                    MainActivity.title.setTextColor(Color.GREEN);
                    MainActivity.title.setText("State:FINE");
                }
                else {
                    MainActivity.title.setTextColor(Color.RED);
                    MainActivity.title.setText("State:EMERGENCY");
                }
            }
        });
        homeViewModel.getExtra().observe(getViewLifecycleOwner(), new Observer<Extra>() {
            @Override
            public void onChanged(Extra extra) {
                Date date = new Date();
                long spentHours = (date.getTime() - extra.getDate().getTime())/ (1000 * 60 * 60);
                lastText.setText(df.format(extra.getDate()));
                lastText.append("\n("+ String.valueOf(spentHours)+")hours spent");
            }
        });
        MainActivity.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.title.setText(R.string.load);
                Map<String, String> map = new HashMap<>();
                map.put("cmd", "update");
                MainActivity.firebaseFirestore.collection("Cmd").document("cmd").set(map).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        return root;
    }
}
