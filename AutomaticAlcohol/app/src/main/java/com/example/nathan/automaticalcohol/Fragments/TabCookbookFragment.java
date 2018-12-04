package com.example.nathan.automaticalcohol.Fragments;

import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Adapters.CookbookResultsRecyclerAdapter;
import com.example.nathan.automaticalcohol.Adapters.CookbookResultsRecyclerAdapter.MyViewHolder;
import com.example.nathan.automaticalcohol.Classes.Drink;
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
    private Button search_button;
    private EditText searchBar_text;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_cookbook, container, false);
        // initialize recycler view for the drink queue
        mRecyclerViewCookbook = view.findViewById(R.id.cookbook_recyclerView);
        mRecyclerAdapterCookbook = new CookbookResultsRecyclerAdapter(getContext(), lstCookbookResults, Constants.DRINKS, recyclerInterface);
        mRecyclerViewCookbook.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewCookbook.setAdapter(mRecyclerAdapterCookbook);
        search_button = view.findViewById(R.id.Search);
        final MyViewHolder vHolder = new MyViewHolder(view);


        search_button.setOnClickListener(new View.OnClickListener() {
             //        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {

                 searchBar_text = view.findViewById(R.id.SearchBar);
                 final String searchText = searchBar_text.getText().toString();

                 // grab drinks and stick them into an array for easier lookup
                 final DatabaseReference drinksRef = mDatabase.getReference("drinks");
                 drinksRef.addListenerForSingleValueEvent(new ValueEventListener() {
                     @Override
                     public void onDataChange(DataSnapshot dataSnapshot) {
                         lstCookbookResults.clear();
                         for(DataSnapshot drinkName : dataSnapshot.getChildren()) {
                             if(searchText.equals(""))
                             {
                                 Drink allDrinks = drinkName.getValue(Drink.class);
                                 lstCookbookResults.add(allDrinks);
                             }
                             else if(drinkName.getValue(Drink.class).getName().equals(searchText))
                             {
                                 Drink matchedDrink = drinkName.getValue(Drink.class);
                                 lstCookbookResults.add(matchedDrink);

                                 // let the recyclerView know it needs to change
                                 mRecyclerAdapterCookbook.notifyDataSetChanged();
                                 break;
                             }
                             else
                             {
                                 lstCookbookResults.clear();
                             }
                             // let the recyclerView know it needs to change
                             mRecyclerAdapterCookbook.notifyDataSetChanged();
                         }
                     }

                     @Override
                     public void onCancelled(DatabaseError firebaseError) {

                     }
                 });

         }});

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
