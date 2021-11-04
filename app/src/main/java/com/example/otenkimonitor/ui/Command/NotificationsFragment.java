package com.example.otenkimonitor.ui.Command;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.otenkimonitor.MainActivity;
import com.example.otenkimonitor.R;

public class NotificationsFragment extends Fragment {
    private NotificationsViewModel notificationsViewModel;
    private TextView textFanf;
    private TextView textFanS;
    boolean isSeekbarChanged = false;

    public View onCreateView(@NonNull LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        textFanf = root.findViewById(R.id.textFANf);
        textFanS = root.findViewById(R.id.textFANs);
        final SeekBar seekf = root.findViewById(R.id.seekBar2);
        final SeekBar seekS = root.findViewById(R.id.seekBar3);
        root.findViewById(R.id.button8).setOnClickListener(onClickListener);
        root.findViewById(R.id.button9).setOnClickListener(onClickListener);
        root.findViewById(R.id.button10).setOnClickListener(onClickListener);
        MainActivity.title.setText("Command Center");
        MainActivity.toolbar.setNavigationIcon(null);

        notificationsViewModel.getFan1().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textFanf.setText(String.valueOf(integer)+ "%");
                if (!isSeekbarChanged) {
                    seekf.setProgress(integer);
                    isSeekbarChanged = true;
                }
            }
        });
        notificationsViewModel.getFan2().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textFanf.setText(String.valueOf(integer)+ "%");
                if (!isSeekbarChanged) {
                    seekS.setProgress(integer);
                    isSeekbarChanged = true;
                }
            }
        });

        seekf.setOnSeekBarChangeListener(this.onSeekBarChangeListener);
        seekS.setOnSeekBarChangeListener(this.onSeekBarChangeListener);

        return root;
    }

    SeekBar.OnSeekBarChangeListener onSeekBarChangeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (!isSeekbarChanged)return;
            switch (seekBar.getId()) {
                case R.id.seekBar2: {
                    notificationsViewModel.sendCmd("fan1" + progress);
                    break;
                }
                case R.id.seekBar3: {
                    notificationsViewModel.sendCmd("fan2" + progress);
                    break;
                }
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {}
        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {}
    };

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //*************************************************************
            /*コマンドは全部小文字にしましょう*/
            //************************************************************
            switch (v.getId()) {
                case R.id.button8: {
                    //sendCmd("exit");
                    break;
                }
                case R.id.button9: {
                    notificationsViewModel.sendCmd("ping");
                    break;
                }
                case R.id.button10: {
                    notificationsViewModel.sendCmd("lightonoff");
                    break;
                }
                case R.id.button11: {
                    notificationsViewModel.sendCmd("picture");
                    break;
                }
            }
        }
    };
}
