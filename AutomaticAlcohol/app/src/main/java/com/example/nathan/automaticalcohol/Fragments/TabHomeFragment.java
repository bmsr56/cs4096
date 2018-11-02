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
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Ingredient;
import com.example.nathan.automaticalcohol.Classes.Loadout;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
import com.example.nathan.automaticalcohol.Adapters.SpecialsRecyclerAdapter;

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

    private GoogleSignInClient mGoogleSignInClient;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefSpecials;
    private DatabaseReference mRefDrinkQueue;

    private String pin;

    private RecyclerInterface recyclerInterface;

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
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);

        editText_custName = view.findViewById(R.id.editText_custName);
        editText_drinkName = view.findViewById(R.id.editText_drinkName);
        spinner_addItem = view.findViewById(R.id.spinner_addItem);
        textView_totalCost = view.findViewById(R.id.textView_totalCost);

//         goes here when order button is clicked
        buttonSubmitOrder = view.findViewById(R.id.buttonSubmitOrder);
        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // TODO: check custName not be null
                // when submit button clicked make sure start ordering process
                String custName = editText_custName.getText().toString();
                order.setName(custName);
                acquireLoadout(order);
            }
        });

        final TextView orderDrink = view.findViewById(R.id.textView_orderDrink);
        spinner_addItem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String addOn = "";
                try {
                    switch (position) {

                        // TODO: each these are going to have to change some "total cost" variable based on price of individual shot

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
                    // TODO: update total drink cost
                    // computeCost(baseDrink, addOn_List?? - MULTIPLE ADD ON SUPPORT??)
                    // TODO: MULTIPLE-ADD-ON SUPPORT??'
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
        mRecyclerAdapterSpecials = new SpecialsRecyclerAdapter(getContext(), lstSpecials, Constants.SPECIALS, recyclerInterface);
        mRecyclerViewSpecials.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewSpecials.setAdapter(mRecyclerAdapterSpecials);


        // TODO: make these 5 button like the most 5 common drinks??
//         logic for handling quick add drinks
        button_quick1 = view.findViewById(R.id.button_quick1);
        button_quick2 = view.findViewById(R.id.button_quick2);
        button_quick3 = view.findViewById(R.id.button_quick3);
        button_quick4 = view.findViewById(R.id.button_quick4);
        button_quick5 = view.findViewById(R.id.button_quick5);

        button_quick1.setText("To Pin Page");
//        button_quick2.setText("Add to Queue");
        button_quick2.setText("Show Date");
        button_quick3.setText("Order Highball");

        // each of these calls a function that orders a drink based on the name of the special
        Log.e(TAG, "onCreateView"+pin);
        button_quick1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "To Pin Page", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        button_quick2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Add to queue", Toast.LENGTH_SHORT).show();
                Toast.makeText(getActivity(), "Button 2", Toast.LENGTH_SHORT).show();



                Date timeStamp1 = new Date();
                Toast.makeText(getActivity(), timeStamp1.toString(), Toast.LENGTH_SHORT).show();

//                lstDrinkQueue.add("new item");
                mRecyclerAdapterDrinkQueue.notifyDataSetChanged();

            }
        });
        button_quick3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Order Highball", Toast.LENGTH_SHORT).show();
                Ingredient ingredient1 = new Ingredient("sprite", 456f);
                Ingredient ingredient2 = new Ingredient("whiskey", 44f);

                ArrayList<Ingredient> ingList = new ArrayList<>();
                ingList.add(ingredient1);
                ingList.add(ingredient2);

//                acquireLoadout(new Drink("Highball", "description", "image", ingList, 1.20f));
//                Drink fart = new Drink("Highball", "description", "image", 1.20f);
//                fart.setIngredients(ingList);
//                acquireLoadout(fart);
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
        mRecyclerAdapterDrinkQueue = new DrinkQueueRecyclerAdapter(getContext(), lstDrinkQueue, Constants.DRINK_QUEUE, recyclerInterface);
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

                // TODO: make order drink read from an order instead of setting here

                // TODO: update this accordingly when we know how database will be setup
                mRefSpecials = mDatabase.getReference("orders");
                // TODO: is the 'Order' Object what gets passed to the drink queue?

                // order goes into the drink queue whenever submit button is pressed
                order = new Order("order1", "", "email", tagName);
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
                    Log.e("TAG11", orderId);

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
     * grabs the loadout from the database and makes it
     * @param order
     */
    private void acquireLoadout(final Order order) {
        Log.e(TAG, "Starting: acquireLoadout");

        final ArrayList<Loadout> loadout = new ArrayList<>();

        // connect to database
        mRefSpecials = mDatabase.getReference("loadout");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    for(DataSnapshot f: data.getChildren()) {

                        // for each element in the loadout in the database grab it's
                        // key (drinkName) and value (amountLeft) and add it to a list
                        Loadout newLoadout = new Loadout(f.getKey(), f.getValue(Long.class));
                        loadout.add(newLoadout);
                        Log.e(TAG, "loadout: "+newLoadout.toString());
                    }
                }
                checkDrinkOrder(order, loadout);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    /**
     * Checks if the drink that is passed in can be made. If so then send off to 'orderDrink'
     * If not then let the user know
     *
     * @param order - order to be made
     * @param loadouts - loadout of what's in the machine
     */
    private void checkDrinkOrder(Order order, ArrayList<Loadout> loadouts) {
        Log.e(TAG, "Starting: checkDrinkOrder");
        boolean makeDrink = true;
        for(String key: order.getDrink().getIngredients().keySet()) {
            Float amount = order.getDrink().getIngredients().get(key);

            boolean check = false;
            for(Loadout loadout: loadouts) {
                Log.e(TAG, "-"+loadout.getBottleName()+"-");
                // check to make sure "ingredient" is in the loadout and there is enough of it to make a drink
                // TODO: error handling for letting user/bartender know there is not enough of something / drink can't be made
                if(loadout.getBottleName().equals(key) && loadout.getAmountLeft() >= amount) {
                    check = true;
                }
            }

            // got through all elements of loadout for specific ingredient
            // if check is false, then ingredient is not in loadout -> can't make the drink
            if(!check) {
                Log.e(TAG, "        checkDrinkOrder -> failed -"+key+"-");
                Toast.makeText(getActivity(), "Drink order could not be made. Check loadout", Toast.LENGTH_SHORT).show();
                makeDrink = false;
                break;
            }
        }

        // if got all the way through without changing "makeDrink" to false
        // means all the ingredients are in the loadout, so make the drink
        if(makeDrink) {
            orderDrink(order);
        }
    }

    /**
     * This will interface with the database to...
     *      - make an order (being kept in the database)
     *      - order the drink (throw it in the drink queue)
     *          - take away x amount of ingredient from database
     *
     * @param order - order to be processed
     */
    // TODO: if we passed "loadouts" from calling then we may not have to search database again...
    // TODO: (cont.) as "loadouts" has the amounts in it.
    private void orderDrink(final Order order) {
        // now have to actually interface with the database

        Log.e(TAG, "Starting: orderDrink");

        // connect to database
        mRefSpecials = mDatabase.getReference("loadout");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(String ingKey: order.getDrink().getIngredients().keySet()) {
                    Float ingAmount = order.getDrink().getIngredients().get(ingKey);

                    // for each ingredient we have to take out the amount that is needed
                    //      from each drink

                    // read the loadout from the database
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        for(DataSnapshot loadoutChild: data.getChildren()) {
                            Log.e(TAG, "    key: "+loadoutChild.getKey());
                            // if the ingredient name matches the key of the loadout stored:
                            //      key: name_of_drink
                            //      value: amount_left
                            // then take ingredient.amount off of what's in the database
                            if(ingKey.equals(loadoutChild.getKey())) {

                                Log.e(TAG, "they equal");

                                Long value = loadoutChild.getValue(Long.class);
                                Float other = value - ingAmount;

                                Log.e(TAG, Float.toString(other));
                                mDatabase.getReference("loadout").child(data.getKey()).child(loadoutChild.getKey()).setValue(other);

                                DatabaseReference orderRef = mDatabase.getReference("order");
                                Toast.makeText(getActivity(), "added order", Toast.LENGTH_SHORT).show();

                                order.setOrderNumber(orderRef.push().getKey());
                                orderRef.child(order.getOrderNumber()).setValue(order);
                                mDatabase.getReference().child("queue").child(order.getOrderNumber()).setValue("1");
                            }
                        }
                    }
                }
                // when it gets here it means all the backend stuff is done, so throw it on the drinkQueue
//                lstDrinkQueue.add(order);
//                mRecyclerAdapterDrinkQueue.notifyDataSetChanged();
                Log.e(TAG, "order completed");
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    /**
     * signs out the bartender from the app, not just the pin
     */
    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // TODO: might throw "logout successful" kind of a thing in here
                    }
                });
    }

}
