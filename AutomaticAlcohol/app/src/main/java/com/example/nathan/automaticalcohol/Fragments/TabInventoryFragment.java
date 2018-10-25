package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Classes.Loadout;
import com.example.nathan.automaticalcohol.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class TabInventoryFragment extends Fragment{
    private static final String TAG = "TabInventoryFragment";

    private EditText et_amountLeft;

    // TODO: is this the position of the bottle??   -> bottleLocation
    private EditText et_bottleNumber;
    private EditText et_bottleName;

    private Button btn_bottleChange;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mLoadoutReference;

    private ArrayList<Loadout> mLoadouts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_inventory, container, false);

        // init Firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mLoadoutReference = mDatabase.getReference("loadout");

        et_amountLeft = view.findViewById(R.id.editText_amountLeft);
        et_bottleNumber = view.findViewById(R.id.editText_bottleNumber);
        et_bottleName = view.findViewById(R.id.editText_bottleName);

        btn_bottleChange = view.findViewById(R.id.button_bottleChange);
        btn_bottleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    int bottleNumber = Integer.parseInt(et_bottleNumber.getText().toString());
                    String bottleName = et_bottleName.getText().toString();


                    String amountLeft = et_amountLeft.getText().toString();
                    Long amt_left = Long.parseLong(amountLeft);


                    // make sure number entered is a valid loadout position
                    if (bottleNumber < 1 || bottleNumber > 6) {
                        Toast.makeText(getActivity(), "Enter valid loadout location", Toast.LENGTH_SHORT).show();
                    } else {
                        // overwrite bottle position with new loadout
                        mLoadoutReference.child(Integer.toString(bottleNumber)).setValue(new Loadout(bottleName, amt_left));
                    }
                } catch (Exception e) {
                    Log.e(TAG, " problem", e);
                }

            }
        });


        // TODO: make data change listener thing for bottle amounts
        // TODO: or should it just look once?...   above is more robust
        // currently just looking once

        final BarChart chart = view.findViewById(R.id.bar_chart);


        mLoadoutReference = mDatabase.getReference("loadout");
        mLoadoutReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    for(DataSnapshot f: data.getChildren()) {
                        // for each element in the loadout in the database grab it's
                        // key (drinkName) and value (amountLeft) and add it to a list
                        Loadout newLoadout = new Loadout(f.getKey(), f.getValue(Long.class));
                        mLoadouts.add(newLoadout);
                        Log.e(TAG, "loadout: "+newLoadout.toString());
                    }
                }




            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        // TODO: connect data grabbed from database to graphs


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
