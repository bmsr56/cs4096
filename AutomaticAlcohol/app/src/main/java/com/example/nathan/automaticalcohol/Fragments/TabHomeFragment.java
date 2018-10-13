package com.example.nathan.automaticalcohol.Fragments;

import android.content.Intent;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android. widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Activities.MainActivity;
import com.example.nathan.automaticalcohol.Activities.PinActivity;
import com.example.nathan.automaticalcohol.Activities.UserActivity;
import com.example.nathan.automaticalcohol.Classes.Color;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerViewAdapter;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirestoreRegistrar;

import java.util.ArrayList;
import java.util.List;

public class TabHomeFragment extends Fragment{
    private static final String TAG = "TabHomeFragment";

    private EditText editText_custName;
    private EditText editText_drinkName;
    private Spinner spinner_addItem;
    private TextView textView_totalCost;
    private Button buttonSubmitOrder;

    private Button button_special1;

    private Button button_quick1;
    private Button button_quick2;
    private Button button_quick3;
    private Button button_quick4;
    private Button button_quick5;

    private RecyclerView mRecyclerViewDrinkQueue;
    private RecyclerViewAdapter mRecyclerAdapterDrinkQueue;
    private List<String> lstDrinkQueue;

    private RecyclerView mRecyclerViewSpecials;
    private RecyclerViewAdapter mRecyclerAdapterSpecials;
    private List<String> lstSpecials;

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mRefSpecials;
    private DatabaseReference mRefDrinkQueue;

    private ArrayList<Color> value;

    private String pin;


    public TabHomeFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);

        editText_custName = view.findViewById(R.id.editText_custName);
        editText_drinkName = view.findViewById(R.id.editText_drinkName);
        spinner_addItem = view.findViewById(R.id.spinner_addItem);
        textView_totalCost = view.findViewById(R.id.textView_totalCost);

//         goes here when order button is clicked
        buttonSubmitOrder = view.findViewById(R.id.buttonSubmitOrder);
        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button submit order", Toast.LENGTH_SHORT).show();

                String custName = editText_custName.getText().toString();
                String drinkName = editText_drinkName.getText().toString();
                String addItem = spinner_addItem.getSelectedItem().toString();

                textView_totalCost.setText(computeCost(addItem));
            }
        });

        final TextView orderDrink = view.findViewById(R.id.textView_orderDrink);
        spinner_addItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    // TODO: each these are going to have to change some "total cost" variable based on price of individual shot

                    case 0:
                        orderDrink.setText("Order Drink:");
                        break;
                    case 1:
                        orderDrink.setText("Tequila");
                        break;
                    case 2:
                        orderDrink.setText("Whiskey");
                        break;
                    case 3:
                        orderDrink.setText("Vodka");
                        break;
                }
                // TODO: update total drink cost
                // computeCost(baseDrink, addOn_List?? - MULTIPLE ADD ON SUPPORT??)
                // TODO: MULTIPLE-ADD-ON SUPPORT??
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


//         logic for handling specials
        button_special1 = view.findViewById(R.id.button_special1);

//         each of these calls a function that orders a drink based on the name of the special
        button_special1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                // TODO: make this look better

                Toast.makeText(getActivity(), "Signs Out", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special1.getText().toString());
                FirebaseAuth.getInstance().signOut();
                signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Log.e(TAG, "LOGGING OUT");
                startActivity(intent);
            }
        });

        // initialize recycler view for bartender drink specials
        mRecyclerViewSpecials = view.findViewById(R.id.specials_recyclerView);
        mRecyclerAdapterSpecials = new RecyclerViewAdapter(getContext(), lstSpecials, Constants.SPECIALS);
        mRecyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewSpecials.setAdapter(mRecyclerAdapterSpecials);



        // TODO: make these 5 button like the most 5 common drinks??
//         logic for handling quick add drinks
        button_quick1 = view.findViewById(R.id.button_quick1);
        button_quick2 = view.findViewById(R.id.button_quick2);
        button_quick3 = view.findViewById(R.id.button_quick3);
        button_quick4 = view.findViewById(R.id.button_quick4);
        button_quick5 = view.findViewById(R.id.button_quick5);

//         each of these calls a function that orders a drink based on the name of the special
        button_quick1.setText(pin);
        Log.e(TAG, "onCreateView"+pin);
        button_quick1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 1", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick1.getText().toString());
            }
        });
        button_quick2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 2", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick2.getText().toString());

                lstDrinkQueue.add("new item");
                mRecyclerAdapterDrinkQueue.notifyDataSetChanged();

            }
        });
        button_quick3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 3", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick3.getText().toString());
            }
        });
        button_quick4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 4", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick4.getText().toString());
            }
        });
        button_quick5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 5", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick5.getText().toString());
            }
        });

        // initialize recycler view for the drink queue
        mRecyclerViewDrinkQueue = view.findViewById(R.id.drinkQueue_recyclerView);
        mRecyclerAdapterDrinkQueue = new RecyclerViewAdapter(getContext(), lstDrinkQueue, Constants.DRINK_QUEUE);
        mRecyclerViewDrinkQueue.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDrinkQueue.setAdapter(mRecyclerAdapterDrinkQueue);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            Log.e(TAG, "getArguments != null");
            pin = getArguments().getString("bartenderPin");
        } else {
            Log.e(TAG, "getArguments == null");
        }
        Log.e(TAG, "onCreate"+pin);


        // initialize the drink queue
        // TODO: this has to be setup to listen for incoming messages to the queue
        /*  connect to database
            for each entry in database
                lstDrinkQueue.add(entry);
         */

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        lstDrinkQueue = new ArrayList<>();
        lstSpecials = new ArrayList<>();


        // read data from
        mRefSpecials = mDatabase.getReference("bartenders").child(mAuth.getUid()).child(pin).child("specials");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Log.e(TAG, data.getValue(String.class));
                    lstSpecials.add(data.getValue(String.class));
                    mRecyclerAdapterSpecials.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });

        // Google Sign In init (used for sign out purposes)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

    }

    private int computeCost(String addItem) {
        return 0;
    }

    private void orderDrink(String drink) {

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // might throw "logout successful" kind of a thing in here
                    }
                });
    }

    private void readFromDatabase() {
        final GenericTypeIndicator<ArrayList<Color>> t = new GenericTypeIndicator<ArrayList<Color>>() {};

        // Read from the database
        mRefSpecials.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.

                value = dataSnapshot.getValue(t);

                for (Color c: value) {
                    Log.d(TAG, "Value is: " + c);
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });



    }
}
