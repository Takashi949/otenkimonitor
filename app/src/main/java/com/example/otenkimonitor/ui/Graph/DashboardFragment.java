package com.example.otenkimonitor.ui.Graph;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.otenkimonitor.Ame;
import com.example.otenkimonitor.MainActivity;
import com.example.otenkimonitor.R;
import com.example.otenkimonitor.Room;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DashboardFragment extends Fragment {
    private DashboardViewModel dashboardViewModel;
    private TextView tt;
    private TextView th;
    private LineChart lineChart;
    private LineData lineData;
    private ArrayList<Entry> entries = new ArrayList<>();
    private ArrayList<Entry> humies = new ArrayList<>();
    private ArrayList<Entry> vol = new ArrayList<>();
    private ArrayList<Entry> Rtempes = new ArrayList<>();
    private ArrayList<Entry> Rhumies = new ArrayList<>();
    private Calendar showDate = Calendar.getInstance();
    private DatePickerDialog dp;
    private SimpleDateFormat TitlePat = new SimpleDateFormat("yyyy年MM月dd日");
    private SimpleDateFormat TextPat = new SimpleDateFormat("HH時mm分");
    String [] lineMenu = {"温湿度", "電圧", "部屋温湿度"};
    int selectitemId = 0;

    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        dashboardViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        dashboardViewModel.getAme().observe(getViewLifecycleOwner(), new Observer<List<Ame>>() {
            @Override
            public void onChanged(List<Ame> ames) {
                entries.clear();
                humies.clear();
                vol.clear();

                for (Ame a :ames){
                    float day = a.getDate().getTime();
                    humies.add(new Entry(day, a.getHumidity()));
                    entries.add(new Entry(day, a.getTemp()));
                    vol.add(new Entry(day, a.getBattery()));
                }
                setData();
            }
        });
        dashboardViewModel.getRoom().observe(getViewLifecycleOwner(), new Observer<List<Room>>() {
            @Override
            public void onChanged(List<Room> rooms) {
                Rtempes.clear();
                Rhumies.clear();
                for (Room a :rooms){
                    float day = a.getDate().getTime();
                    Rhumies.add(new Entry(day, a.getHumidity()));
                    Rtempes.add(new Entry(day, a.getTemp()));
                }
                setData();
            }
        });

        MainActivity.toolbar.setNavigationIcon(null);
        MainActivity.title.setText(TitlePat.format(showDate.getTime()));
        MainActivity.title.setTextColor(Color.GREEN);
        MainActivity.toolbar.setOnClickListener(this.onClickListener);
        dp = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                showDate.set(Calendar.YEAR, i);
                showDate.set(Calendar.MONTH, i1);
                showDate.set(Calendar.DAY_OF_MONTH, i2);

                MainActivity.title.setText(DashboardFragment.this.TitlePat.format(showDate.getTime()));

                lineData.clearValues();
                lineChart.invalidate();
                lineData.notifyDataChanged();
                dashboardViewModel.LoadDate(showDate);
                lineChart.invalidate();
                lineData.notifyDataChanged();
            }
        }, showDate.get(Calendar.YEAR),showDate.get(Calendar.MONTH),showDate.get(Calendar.DAY_OF_MONTH));

        tt = root.findViewById(R.id.DataTemp);
        th = root.findViewById(R.id.DataHum);

        Spinner spinner = root.findViewById(R.id.spinner);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), R.layout.support_simple_spinner_dropdown_item, lineMenu);
        arrayAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selectitemId = i;
                if(i == 0 || i == 2){
                    setHumtemp();
                    setData();
                }
                else if(i == 1){
                    setBat();
                    setData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        lineChart = root.findViewById(R.id.lchart);
        setHumtemp();
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        MainActivity.toolbar.setOnClickListener(null);
    }

    private void setHumtemp(){
        lineChart.setDrawGridBackground(true);
        lineChart.setDragEnabled(true);
        lineChart.getDescription().setEnabled(false);

        // Grid縦軸を破線
        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateAxisFomatter());

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(40f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // 右側の目盛り
        lineChart.getAxisRight().setEnabled(true);
        lineChart.getAxisRight().setAxisMaximum(100f);
        lineChart.getAxisRight().setAxisMinimum(0f);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        lineChart.animateX(2500);
    }
    private void setBat(){
        lineChart.setDrawGridBackground(true);
        lineChart.setDragEnabled(true);
        lineChart.getDescription().setEnabled(false);

        // Grid縦軸を破線
        XAxis xAxis = lineChart.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new DateAxisFomatter());

        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setAxisMaximum(18f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(true);

        // 右側の目盛り
        lineChart.getAxisRight().setEnabled(false);

        Legend l = lineChart.getLegend();
        l.setForm(Legend.LegendForm.LINE);
        l.setTextColor(Color.BLACK);

        lineChart.animateX(2500);
    }

    private void setData() {
        LineDataSet lineDataSet = new LineDataSet(entries, "温度");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet HumSet = new LineDataSet(humies, "湿度");
        HumSet.setColor(Color.BLUE);
        HumSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        dataSets.add(HumSet);

        LineDataSet VolSet = new LineDataSet(vol, "電圧");
        VolSet.setColor(Color.RED);
        VolSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        ArrayList<ILineDataSet> VolSets = new ArrayList<>();
        VolSets.add(VolSet);

        LineDataSet RtempSet = new LineDataSet(Rtempes, "部屋温度");
        RtempSet.setColor(Color.RED);
        RtempSet.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineDataSet RHumSet = new LineDataSet(Rhumies, "部屋湿度");
        RHumSet.setColor(Color.BLUE);
        RHumSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

        ArrayList<ILineDataSet> RSets = new ArrayList<>();
        RSets.add(RtempSet);
        RSets.add(RHumSet);

        if(selectitemId == 0){
            lineData = new LineData(dataSets);
        }
        else if (selectitemId == 1){
            lineData = new LineData(VolSets);
        }
        else if(selectitemId == 2){
            lineData = new LineData(RSets);
        }
        lineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                Calendar xc = Calendar.getInstance();
                xc.setTimeInMillis((long)e.getX());
                tt.setText(TextPat.format(xc.getTime()));
                th.setText(String.valueOf(e.getY()));
            }

            @Override
            public void onNothingSelected() {}
        });
        lineChart.setData(lineData);
        lineChart.invalidate();
        lineData.notifyDataChanged();
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
           if (v.getId() == R.id.toolbar){
                dp.show();
           }
        }
    };
}
