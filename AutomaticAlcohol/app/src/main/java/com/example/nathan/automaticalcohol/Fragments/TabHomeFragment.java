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
import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Ingredient;
import com.example.nathan.automaticalcohol.Classes.Loadout;
import com.example.nathan.automaticalcohol.Classes.Order;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;
import com.example.nathan.automaticalcohol.RecyclerInterface;
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
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

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

    private String pin;

    private RecyclerInterface recyclerInterface;

    private TextView bs;


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
        mRecyclerAdapterSpecials = new RecyclerViewAdapter(getContext(), lstSpecials, Constants.SPECIALS, recyclerInterface);
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
        button_quick2.setText("Add to Queue");
        button_quick3.setText("Order Highball");

//         each of these calls a function that orders a drink based on the name of the special
        Log.e(TAG, "onCreateView"+pin);
        button_quick1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "To Pin Page", Toast.LENGTH_SHORT).show();
                getActivity().onBackPressed();
            }
        });
        button_quick2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Add to queue", Toast.LENGTH_SHORT).show();
                lstDrinkQueue.add("new item");
                mRecyclerAdapterDrinkQueue.notifyDataSetChanged();

            }
        });
        button_quick3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Order Highball", Toast.LENGTH_SHORT).show();
                Ingredient ingredient1 = new Ingredient("sprite", 456L);
                Ingredient ingredient2 = new Ingredient("whiskey", 44L);

                ArrayList<Ingredient> ingList = new ArrayList<>();
                ingList.add(ingredient1);
                ingList.add(ingredient2);

                acquireLoadout(new Drink("description", "image", ingList, "1.2"));
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
        mRecyclerAdapterDrinkQueue = new RecyclerViewAdapter(getContext(), lstDrinkQueue, Constants.DRINK_QUEUE, recyclerInterface);
        mRecyclerViewDrinkQueue.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerViewDrinkQueue.setAdapter(mRecyclerAdapterDrinkQueue);

        bs = view.findViewById(R.id.textView_bartenderSpecials);


        return view;
    }


    /**
     * more stuff needed by android to make the screen and connecting logic
     * @param savedInstanceState
     */
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


        recyclerInterface = new RecyclerInterface() {
            @Override
            public void onTagClicked(String tagName) {
                bs.setText(tagName);
                mRefSpecials = mDatabase.getReference("drinks").child(tagName).child("ingredients");
                mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot data: dataSnapshot.getChildren()){

                            /*  this is grabbing the drink ingredients from the "drinks" table
                                of the drink that was clicked on in EITHER of the recyclerViews
                                TODO: we should probably have one of these for each of the recyclerViews
                                TODO: (cont.) as we will probably (maybe) need to process clicks separately
                            */


                            /*  right now everything that is coming in here is going to be
                                an amount for an ingredient for every ingredient in the "ingredients"
                                portion of the drinks.<drink_name> 'table'
                             */
                            Long num = data.getValue(Long.class);
                            String d = Long.toString(num);
                            Log.e(TAG, "-"+d+"-");
                            lstDrinkQueue.add(d);
                            mRecyclerAdapterDrinkQueue.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError firebaseError) {

                    }
                });

            }
        };

    }

    /**
     * Computes cost of add-ons and sends a new amount to the left most page on this fragment
     * to update the cost of an ordered drink
     * @param addItem - item to be added on, comes from the spinner
     * @return
     */
    private int computeCost(String addItem) {
        return 0;
    }


    /**
     * grabs the loadout from the database and makes it
     * @param drink
     */
    private void acquireLoadout(final Drink drink) {
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
                checkDrinkOrder(drink, loadout);
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    private void checkDrinkOrder(Drink drink, ArrayList<Loadout> loadouts) {
        Log.e(TAG, "Starting: checkDrinkOrder");
        boolean makeDrink = true;
        for(Ingredient ingredient: drink.getIngredients()) {
            boolean check = false;
            for(Loadout loadout: loadouts) {
                Log.e(TAG, "-"+loadout.getBottleName()+"-");
                // check to make sure "ingredient" is in the loadout and there is enough of it to make a drink
                // TODO: error handling for letting user/bartender know there is not enough of something / drink can't be made
                if(loadout.getBottleName().equals(ingredient.getName()) && loadout.getAmountLeft() >= ingredient.getAmount()) {
                    check = true;
                }
            }

            // got through all elements of loadout for specific ingredient
            // if check is false, then ingredient is not in loadout -> can't make the drink
            if(!check) {
                Log.e(TAG, "        checkDrinkOrder -> failed -"+ingredient.getName()+"-");
                makeDrink = false;
                break;
            }
        }

        // if got all the way through without changing "makeDrink" to false
        // means all the ingredients are in the loadout, so make the drink
        if(makeDrink) {
            orderDrink(drink);
        }
    }

    /**
     * This will interface with the database to...
     *      - make an order (being kept in the database)
     *      - order the drink (throw it in the drink queue)
     *          - take away x amount of ingredient from database
     *
     * @param drink - the drink to be made
     */
    // TODO: if we passed "loadouts" from calling then we may not have to search database again...
    // TODO: (cont.) as "loadouts" has the amounts in it.
    private void orderDrink(final Drink drink) {
        // now have to actually interface with the database

        Log.e(TAG, "Starting: orderDrink");

        // connect to database
        mRefSpecials = mDatabase.getReference("loadout");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(Ingredient ingredient: drink.getIngredients()) {
                    // for each ingredient we have to take out the amount that is needed from the
                    // about that the loadout says the database has

                    Log.e(TAG, "orderDrink - "+ingredient.getName());

                    // read the loadout from the database
                    for(DataSnapshot data: dataSnapshot.getChildren()){
                        for(DataSnapshot f: data.getChildren()) {
                            Log.e(TAG, "    key: "+f.getKey());
                            // if the ingredient name matches the key of the loadout stored:
                            //      key: name_of_drink
                            //      value: amount_left
                            // then take ingredient.amount off of what's in the database
                            if(ingredient.getName().equals(f.getKey())) {

                                Log.e(TAG, "they equal");

                                Long value = f.getValue(Long.class);
                                Long other = value-ingredient.getAmount();

                                Log.e(TAG, Long.toString(other));
                                mDatabase.getReference("loadout").child(data.getKey()).child(f.getKey()).setValue(Long.toString(other));
                            }
                        }
                    }
                }

                // TODO: update this accordingly when we know how database will be setup
                mRefSpecials = mDatabase.getReference("orders");
                Order order = new Order();
                // TODO: is the 'order' what gets passed to the drink queue?
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
