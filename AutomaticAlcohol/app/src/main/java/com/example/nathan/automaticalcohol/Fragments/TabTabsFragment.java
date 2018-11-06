package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class TabTabsFragment extends Fragment{
    private static final String TAG = "TabTabsFragment";

    private Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_tabs, container, false);

        //button logic


        //spinner views
        //recycler

        return view;
    }

    //onCreate would be where back end ; logic like with the database and whatnot
    /**
     * more stuff needed by android to make the screen and connecting logic
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");

        // start listening to the 'orderRef'
        ValueEventListener singleEventListener = new ValueEventListener() {
            //look at tabhomefragment for an example of how to grab the data from the db
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        //orderRef.addListenerForSingleValueEvent(singleEventListener);
    }




}
