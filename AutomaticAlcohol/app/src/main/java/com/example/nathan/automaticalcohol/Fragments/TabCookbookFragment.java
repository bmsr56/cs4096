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
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Adapters.CookbookResultsRecyclerAdapter;
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.example.nathan.automaticalcohol.Constants;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class TabCookbookFragment extends Fragment {
    private static final String TAG = "TabCookbookFragment";
    private RecyclerView mRecyclerViewCookbook;
    private CookbookResultsRecyclerAdapter mRecyclerAdapterCookbook;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mDrinks;
    private HashMap<String, Float> shotPrice = new HashMap<>();
    private RecyclerInterface recyclerInterface;
    private List<Drink> lstCookbookResults = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_cookbook, container, false);
        // initialize recycler view for the drink queue
        mRecyclerViewCookbook = view.findViewById(R.id.cookbook_recyclerView);
        mRecyclerAdapterCookbook = new CookbookResultsRecyclerAdapter(getContext(), lstCookbookResults, Constants.DRINKS, recyclerInterface);
        mRecyclerViewCookbook.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCookbook.setAdapter(mRecyclerAdapterCookbook);


        // grab shot prices and stick them into an array for easier lookup
        final DatabaseReference orderRef = mDatabase.getReference("drinks");
        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot drinkName : dataSnapshot.getChildren()) {
                    // if the order id matches an 'Order Object' then remember it
                        Drink fart = drinkName.getValue(Drink.class);
                        lstCookbookResults.add(fart);
                }

                // let the recyclerView know it needs to change
                mRecyclerAdapterCookbook.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        return view;
    }


    /**
     * more stuff needed by android to make the screen and connecting logic
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e(TAG, "onCreate");



    }
}
