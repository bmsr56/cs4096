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

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import com.github.mikephil.charting.formatter.AxisValueFormatter;
import java.util.ArrayList;

public class TabInventoryFragment extends Fragment{
    private static final String TAG = "TabInventoryFragment";

    private EditText et_amountLeft;
    private EditText et_bottleNumber;
    private EditText et_bottleName;

    private Button btn_bottleChange;
    private ArrayList<BarEntry> barEntries = new ArrayList<>();

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mLoadoutReference;

    private ArrayList<Loadout> mLoadouts = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_inventory, container, false);

        // init Firebase instance
        mAuth = FirebaseAuth.getInstance();

        // grab the loadout from the database
        mLoadoutReference = mDatabase.getReference("loadout");

        // this assigns variable names to the text fields
        et_amountLeft = view.findViewById(R.id.editText_amountLeft);
        et_bottleNumber = view.findViewById(R.id.editText_bottleNumber);
        et_bottleName = view.findViewById(R.id.editText_bottleName);

        // submit change button, the listener will add changes to database
        btn_bottleChange = view.findViewById(R.id.button_bottleChange);
        btn_bottleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try{
                    // grabs info from the screen
                    int bottleNumber = Integer.parseInt(et_bottleNumber.getText().toString());
                    String bottleName = et_bottleName.getText().toString();
                    String amountLeft = et_amountLeft.getText().toString();
                    Long amt_left = Long.parseLong(amountLeft);

                    // clear the input
                    et_amountLeft.setText("");
                    et_bottleName.setText("");
                    et_bottleNumber.setText("");

                    Toast.makeText(getActivity(), "Info updated. Graph will update on refresh.", Toast.LENGTH_SHORT).show();

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

        //initializes the inventory chart
        final BarChart inventoryChart = view.findViewById(R.id.bar_chart);

        //add new entries to the barchart
        final BarDataSet dataSet = new BarDataSet(barEntries, "Projects");

        //contains the labels for the drink names
        final ArrayList<String> labels = new ArrayList<>();

        //turns off the y axis numbering on right side
        YAxis yAxisRight = inventoryChart.getAxisRight();
        yAxisRight.setDrawLabels(false);

        //sets up the y axis numbering on the left side
        YAxis yAxisLeft = inventoryChart.getAxisLeft();
        yAxisLeft.mAxisMinimum = 0;
        yAxisLeft.mAxisMaximum = 1500;
        yAxisLeft.setStartAtZero(true);
        yAxisLeft.setTextSize(20f);
        inventoryChart.getAxisLeft().setDrawGridLines(false);
        inventoryChart.getXAxis().setDrawGridLines(false);

        //set up the x axis
        final XAxis xAxis = inventoryChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(20f);

        //set up bar display configurations
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        inventoryChart.setFitBars(true);
        inventoryChart.setDescription("Inventory of Bottles");

        mLoadoutReference = mDatabase.getReference("loadout");

        //pulls loadout data from the database for displaying graphs
        mLoadoutReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int iter = 0;

                //clear out all the bar entries and labels
                barEntries.clear();
                labels.clear();

                //gets all the data from the database and puts it to be loaded into the graphs
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Loadout loadout = data.getValue(Loadout.class);
                    labels.add(loadout.getBottleName());
                    barEntries.add(new BarEntry(iter, loadout.getAmountLeft()));
                    iter++;
                }

                //takes the database data and puts into bar data
                BarData data = new BarData(dataSet);
                data.setBarWidth(0.9f);
                inventoryChart.setData(data);

                //This is necessary to put labels on the graphs
                xAxis.setValueFormatter(new AxisValueFormatter(){
                    @Override
                    public String getFormattedValue(float value, AxisBase axis) {
                        return labels.get((int) value);
                    }
                    // we don't draw numbers, so no decimal digits needed
                    @Override
                    public int getDecimalDigits() {  return 0; }
                });
                
                //reloads the chart with all the changes
                inventoryChart.invalidate();
                Log.e(TAG, "It's all done");
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
        inventoryChart.invalidate();
        return view;
    }

}
