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
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Activities.MainActivity;
import com.example.nathan.automaticalcohol.Classes.Color;
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
    private Button button_special2;
    private Button button_special3;
    private Button button_special4;
    private Button button_special5;

    private Button button_quick1;
    private Button button_quick2;
    private Button button_quick3;
    private Button button_quick4;
    private Button button_quick5;

    private RecyclerView myRecyclerView;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private List<String> lstDrinkQueue;

    private GoogleSignInClient mGoogleSignInClient;

    FirebaseDatabase mDatabase;
    DatabaseReference mRef;

    private ArrayList<Color> value;



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
        button_special2 = view.findViewById(R.id.button_special2);
        button_special3 = view.findViewById(R.id.button_special3);
        button_special4 = view.findViewById(R.id.button_special4);
        button_special5 = view.findViewById(R.id.button_special5);

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
        button_special2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button special 2", Toast.LENGTH_SHORT).show();
                orderDrink(button_special2.getText().toString());
                }
        });
        button_special3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button special 3", Toast.LENGTH_SHORT).show();
                orderDrink(button_special3.getText().toString());
            }
        });
        button_special4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button special 4", Toast.LENGTH_SHORT).show();
                orderDrink(button_special4.getText().toString());
            }
        });
        button_special5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button special 5", Toast.LENGTH_SHORT).show();
                orderDrink(button_special5.getText().toString());
            }
        });


//         logic for handling quick add drinks
        button_quick1 = view.findViewById(R.id.button_quick1);
        button_quick2 = view.findViewById(R.id.button_quick2);
        button_quick3 = view.findViewById(R.id.button_quick3);
        button_quick4 = view.findViewById(R.id.button_quick4);
        button_quick5 = view.findViewById(R.id.button_quick5);

//         each of these calls a function that orders a drink based on the name of the special
        button_quick1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 1", Toast.LENGTH_SHORT).show();
                readFromDatabase();
                orderDrink(button_quick1.getText().toString());
            }
        });
        button_quick2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 2", Toast.LENGTH_SHORT).show();
                orderDrink(button_quick2.getText().toString());
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

        myRecyclerView = view.findViewById(R.id.drinkQueue_recyclerView);
        mRecyclerViewAdapter = new RecyclerViewAdapter(getContext(), lstDrinkQueue);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        myRecyclerView.setAdapter(mRecyclerViewAdapter);

        return view;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // initialize the drink queue
        // TODO: this has to be setup to listen for incoming messages to the queue
        /*  connect to database
            for each entry in database
                lstDrinkQueue.add(entry);
         */

        mDatabase = FirebaseDatabase.getInstance();


        mRef = mDatabase.getReference("colors");


        lstDrinkQueue = new ArrayList<>();

        lstDrinkQueue.add("first");
        lstDrinkQueue.add("second");
        lstDrinkQueue.add("third");
        lstDrinkQueue.add("fourth");
        lstDrinkQueue.add("fifth");
        lstDrinkQueue.add("sixth");



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
        mRef.addValueEventListener(new ValueEventListener() {
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
