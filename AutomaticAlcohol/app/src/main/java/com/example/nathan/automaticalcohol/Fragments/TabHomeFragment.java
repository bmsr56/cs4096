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
import com.example.nathan.automaticalcohol.Adapters.DrinkQueueRecyclerAdapter;
import com.example.nathan.automaticalcohol.Adapters.ReportsAdapter;
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Ingredient;
import com.example.nathan.automaticalcohol.Classes.Loadout;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.example.nathan.automaticalcohol.Adapters.SpecialsRecyclerAdapter;

import com.example.nathan.automaticalcohol.ReportsInterface;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TabHomeFragment extends Fragment{
    private static final String TAG = "TabHomeFragment";

    // buttons and variables and such
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
    private DrinkQueueRecyclerAdapter mRecyclerAdapterDrinkQueue;
    private List<Order> lstDrinkQueue = new ArrayList<>();

    private RecyclerView mRecyclerViewSpecials;
    private SpecialsRecyclerAdapter mRecyclerAdapterSpecials;
    private List<Drink> lstSpecials = new ArrayList<>();

    private RecyclerView mRecyclerViewQuick;
    private ReportsAdapter mRecyclerAdapterQuick;
    private List<Drink> lstQuick = new ArrayList<>();

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefSpecials;
    private DatabaseReference mRefDrinkQueue;

    private String pin;

    private RecyclerInterface recyclerInterface;
    private ReportsInterface reportsInterface;

    private Order order;
    private HashMap<String, Float> shotPrice = new HashMap<>();


    /**
     * empty default constructor
     */
    public TabHomeFragment() {
    }


    /**
     * needed by android
     * it's what creates what we see on the screen
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_tab_home, container, false);

        // init textviews and such
        editText_custName = view.findViewById(R.id.editText_custName);
        editText_drinkName = view.findViewById(R.id.editText_drinkName);
        spinner_addItem = view.findViewById(R.id.spinner_addItem);
        textView_totalCost = view.findViewById(R.id.textView_totalCost);

//         goes here when order button is clicked
        buttonSubmitOrder = view.findViewById(R.id.buttonSubmitOrder);
        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
//        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                // when submit button clicked make sure start ordering process
                String custName = editText_custName.getText().toString();
                if(custName.isEmpty()) {
                    Toast.makeText(getActivity(), "Name can't be blank", Toast.LENGTH_SHORT).show();
                } else {
                    // clear the inputs
                    editText_custName.setText("");
                    editText_drinkName.setText("");
                    spinner_addItem.setSelection(0);

                    // drink order attempt
                    order.setName(custName);
                    order.acquireLoadout(order, view);
                }

            }
        });

        spinner_addItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String addOn = "";
                try {
                    switch (position) {
                        case 0:
                            addOn = "none";
                            break;
                        case 1:
                            addOn = "tequila";
                            break;
                        case 2:
                            addOn = "whiskey";
                            break;
                        case 3:
                            addOn = "vodka";
                            break;
                    }
                    Float updatedCost = updateCost(order, addOn);
                    textView_totalCost.setText(String.format(Locale.US, "$ %.2f", updatedCost));
                } catch (Exception e) {
                    Log.e(TAG, "Error: ", e);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //   logic for handling specials
        button_special1 = view.findViewById(R.id.button_special1);
        // each of these calls a function that orders a drink based on the name of the special
        button_special1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Signs Out", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();
                signOut();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Log.e(TAG, "LOGGING OUT");
                startActivity(intent);
            }
        });

        // initialize recycler view for bartender drink specials
        mRecyclerViewSpecials = view.findViewById(R.id.specials_recyclerView);
        mRecyclerAdapterSpecials = new SpecialsRecyclerAdapter(getContext(), lstSpecials, Constants.SPECIALS, recyclerInterface);
        mRecyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewSpecials.setAdapter(mRecyclerAdapterSpecials);

        mRecyclerViewQuick = view.findViewById(R.id.quick_recyclerView);
        mRecyclerAdapterQuick = new ReportsAdapter(getContext(), lstQuick, Constants.QUICK, reportsInterface);
        mRecyclerViewQuick.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewQuick.setAdapter(mRecyclerAdapterQuick);

        button_quick1 = view.findViewById(R.id.button_quick1);
        button_quick2 = view.findViewById(R.id.button_quick2);
        button_quick3 = view.findViewById(R.id.button_quick3);
        button_quick4 = view.findViewById(R.id.button_quick4);
        button_quick5 = view.findViewById(R.id.button_quick5);

        button_quick1.setText("To Pin Page");
        button_quick2.setText(" -- This Still");
        button_quick3.setText("Needs");
        button_quick4.setText("To Be");
        button_quick5.setText("Implemented");

        // each of these calls a function that orders a drink based on the name of the special
        Log.e(TAG, "onCreateView"+pin);
        button_quick1 = view.findViewById(R.id.button_quick1);
        button_quick1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "To Pin Page", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        button_quick2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 2", Toast.LENGTH_SHORT).show();
            }
        });
        button_quick3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 3", Toast.LENGTH_SHORT).show();
            }
        });
        button_quick4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 4", Toast.LENGTH_SHORT).show();
            }
        });
        button_quick5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Testing button quick 5", Toast.LENGTH_SHORT).show();
            }
        });

        // initialize recycler view for the drink queue
        mRecyclerViewDrinkQueue = view.findViewById(R.id.drinkQueue_recyclerView);
        mRecyclerAdapterDrinkQueue = new DrinkQueueRecyclerAdapter(getContext(), lstDrinkQueue, recyclerInterface);
        mRecyclerViewDrinkQueue.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDrinkQueue.setAdapter(mRecyclerAdapterDrinkQueue);

        // grab shot prices and stick them into an array for easier lookup
        mRefSpecials = mDatabase.getReference("extra");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()) {
                    // for each of the children
                    Log.e("TAG12", data.getKey()+"  "+data.getValue(Float.class));
                    shotPrice.put(data.getKey(), data.getValue(Float.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
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

        // grabs the data passed from the bartender activity
        // data is passed as key-value pairs
        if (getArguments() != null) {
            Log.e(TAG, "getArguments != null");
            // if the string passed was passed with key 'Constants.BARTENDER_TO_HOME_TAB_PIN' then it grabs the value and assigns it to 'pin'
            pin = getArguments().getString(Constants.BARTENDER_TO_HOME_TAB_PIN);
        } else {
            Log.e(TAG, "getArguments == null");
        }
        Log.e(TAG, "onCreate"+pin);


        /*
         *  this inputs drinks into the Specials array that is what the "Bartender Specials" section is made of
          */
        // initialize database references
        DatabaseReference specialsRef = mDatabase.getReference("bartenders").child(mAuth.getUid()).child(pin).child("specials");
        final DatabaseReference drinkRef = mDatabase.getReference().child("drinks");
        // add event listener for attached database reference (specialsRef)
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // for each child of specials (root -> bartender -> id -> pin -> specials)
                for(DataSnapshot specialsName: dataSnapshot.getChildren()) {
                    // grab the name of the drink (called it Id cause wasn't sure if name was taken)
                    final String drinkId = specialsName.getValue(String.class);
                    // add event listener for attached database reference (drinkRef)
                    ValueEventListener eventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // for each child of drinks (root -> drinks)
                            for(DataSnapshot drinkName : dataSnapshot.getChildren()) {
                                // if the key of the special is the same as the key of the drink child
                                if(drinkName.getKey().equals(drinkId)) {
                                    // add it to the list
                                    Drink fart = drinkName.getValue(Drink.class);
                                    lstSpecials.add(fart);
                                    // update the recyclerView
                                    mRecyclerAdapterSpecials.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    drinkRef.addListenerForSingleValueEvent(eventListener);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        specialsRef.addListenerForSingleValueEvent(valueEventListener);

        // Google Sign In init (used for sign out purposes)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

        recyclerInterface = new RecyclerInterface() {
            @Override
            public void onTagClicked(Order order) {
                // TODO: Make this happen when a "Drink Queue" object is clicked
            }

            @Override
            public void onTagClicked(final Drink tagName) {
                // change "Order Drink" column update according to special clicked
                editText_drinkName.setText(tagName.getName());
                textView_totalCost.setText(String.format(Locale.US, "$ %.2f", tagName.getPrice()));

                // order goes into the drink queue whenever submit button is pressed
                order = new Order("order1", "", "email", tagName);
            }
        };

        // TODO: actually implement this
        reportsInterface = new ReportsInterface() {
            @Override
            public void fartFunction(String fart) {
                Toast.makeText(getActivity(), fart, Toast.LENGTH_SHORT).show();
            }
        };

        // grab a section of the database
        DatabaseReference drinkQueueRef = mDatabase.getReference("queue");
        // this has to be marked 'final' because it is going to be used in an inner class
        final DatabaseReference orderRef = mDatabase.getReference("order");
        // start listening on the 'drinkQueueRef'
        ValueEventListener valueEventListener1 = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // clear what was in the queue as to not re-add the same drinks
                lstDrinkQueue.clear();
                for(DataSnapshot specialsName: dataSnapshot.getChildren()) {
                    // id of the order in the queue
                    final String orderId = specialsName.getKey();
                    Log.e(TAG, "first for 'listening for queue changes'");

                    // start listening to the 'orderRef'
                    ValueEventListener singleEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot drinkName : dataSnapshot.getChildren()) {
                                // if the order id matches an 'Order Object' then remember it
                                if(drinkName.getKey().equals(orderId)) {
                                    Order fart = drinkName.getValue(Order.class);
                                    lstDrinkQueue.add(fart);
                                }
                            }

                            // sort the queue by time
                            // is sorted by time because that's how 'compare' is written in Order Class
                            Collections.sort(lstDrinkQueue);
                            // let the recyclerView know it needs to change
                            mRecyclerAdapterDrinkQueue.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    };
                    orderRef.addListenerForSingleValueEvent(singleEventListener);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };
        drinkQueueRef.addValueEventListener(valueEventListener1);

    }

    /**
     * Computes cost of add-ons and sends a new amount to the left most page on this fragment
     * to update the cost of an ordered drink
     * @param addItem - item to be added on, comes from the spinner
     * @return cost of drink after adding item
     */
    private Float updateCost(final Order order, final String addItem) {
        if(addItem.equals("none")) {
            order.setAddOn("", 0f);
        } else {
            order.setAddOn("", shotPrice.get(addItem));
        }
        return order.getTotalPrice();
    }


    /**
     * signs out the bartender from the app, not just the pin
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity(), "Logged out successfully!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
