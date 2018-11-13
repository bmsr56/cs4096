package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import com.example.nathan.automaticalcohol.Adapters.DrinkQueueRecyclerAdapter;
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TabTabsFragment extends Fragment{
    private static final String TAG = "TabTabsFragment";

    private Button button;

    private RecyclerView mRecyclerViewTabs;
    private DrinkQueueRecyclerAdapter mRecyclerAdapterTabs;
    private List<Order> lstTabs = new ArrayList<>();

    private RecyclerView mRecyclerViewNames;
    private DrinkQueueRecyclerAdapter mRecyclerAdapterNames;
    private List<Order> lstNames = new ArrayList<>();

    private RecyclerInterface recyclerInterface;

    private Map<String, String> queueData;
    private Map<String, Object> orderData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_tabs, container, false);

        //button logic


        //spinner views
        //recycler

        // connects the layout to the java code
        mRecyclerViewTabs = view.findViewById(R.id.drinkQueue_recyclerView);
        // makes a new adapter that connects the layout to a list
        mRecyclerAdapterTabs = new DrinkQueueRecyclerAdapter(getContext(), lstTabs, Constants.TABS, recyclerInterface);
        // sets how the layout should look
        //mRecyclerViewTabs.setLayoutManager(new LinearLayoutManager(getActivity()));
        // sets the adapter (what is run when the RecyclerView is clicked)
        //mRecyclerViewTabs.setAdapter(mRecyclerAdapterTabs);






        return view;
    }


    /**
     * more stuff needed by android to make the screen and connecting logic
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference queueRef = database.getReference("queue");
        DatabaseReference orderRef = database.getReference("order");
        //Map<String, String> data;
        /*orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orderData = (Map<String, Object>)dataSnapshot.getValue();
            }

            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        orderRef.addValueEventListener(new ValueEventListener() {
            //look at tabhomefragment for an example of how to grab the data from the db
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //get the queued orders
                //orderData = (Map<String, Object>)dataSnapshot.getValue();
                //check to make sure that the keyset is as expected
                //Log.d(TAG, "Value is: " + orderData.keySet());

                //iterate through the keys in the queue
                /*for ( String key : orderData.keySet() ) {
                    //check to see if the key is in the orders table
                    Log.d(TAG, "Entire value string is: " + orderData.get(key));
                    Log.d(TAG, "Date string is: " + orderData.get(key).get("date"));

                }*/

                for(DataSnapshot order : dataSnapshot.getChildren()) {
                    Order currOrder = order.getValue(Order.class);
                    Log.e(TAG, "asdflijhasflkhfdsa");
                    Log.e(TAG, currOrder.getOrderNumber());
                    Log.e(TAG, currOrder.getName());
                    Log.e(TAG, currOrder.getEmail());
                    Log.e(TAG, currOrder.getTotalPrice().toString());
                    Log.e(TAG, "drink:" + currOrder.getDrink().getName());
                    Log.e(TAG, currOrder.getDate().toString());
                    //Log.e(TAG, currOrder.getEmail());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*// start listening to the 'orderRef'
        ValueEventListener singleEventListener = new ValueEventListener() {
            //look at tabhomefragment for an example of how to grab the data from the db
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };*/
        //orderRef.addListenerForSingleValueEvent(singleEventListener);


        // connect to database
        // cycle through items of children 








    }





}
