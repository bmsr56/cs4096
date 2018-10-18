package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nathan.automaticalcohol.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TabInventoryFragment extends Fragment{
    private static final String TAG = "TabInventoryFragment";

    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_inventory, container, false);

        BarChart chart = (BarChart) view.findViewById(R.id.bar_chart);

        ArrayList<BarEntry> BarEntry = new ArrayList<>();
        BarEntry.add(new BarEntry(2f, 0));
        BarEntry.add(new BarEntry(4f, 1));
        BarEntry.add(new BarEntry(6f, 2));
        BarEntry.add(new BarEntry(8f, 3));
        BarEntry.add(new BarEntry(7f, 4));
        BarEntry.add(new BarEntry(3f, 5));

        BarDataSet dataSet = new BarDataSet(BarEntry, "Projects");

        ArrayList<String> labels = new ArrayList<>();
        labels.add("January");
        labels.add("February");
        labels.add("March");
        labels.add("April");
        labels.add("May");
        labels.add("June");

        BarData data = new BarData(dataSet);
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        chart.setData(data);

        chart.setDescription("No of Projects");
        return view;
    }
}
