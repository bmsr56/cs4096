package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.nathan.automaticalcohol.Adapters.DrinkQueueRecyclerAdapter;
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_tabs, container, false);


        // connects the layout to the java code
        mRecyclerViewTabs = view.findViewById(R.id.drinkQueue_recyclerView);
        // makes a new adapter that connects the layout to a list
        mRecyclerAdapterTabs = new DrinkQueueRecyclerAdapter(getContext(), lstTabs, Constants.TABS, recyclerInterface);
        // sets how the layout should look
        mRecyclerViewTabs.setLayoutManager(new LinearLayoutManager(getActivity()));
        // sets the adapter (what is run when the RecyclerView is clicked)
        mRecyclerViewTabs.setAdapter(mRecyclerAdapterTabs);






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


        // connect to database
        // cycle through items of children 







    }







}
