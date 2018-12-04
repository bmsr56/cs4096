package com.example.nathan.automaticalcohol.Classes;

import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;


public class Order implements Comparable<Order>{
    private static final String TAG = "OrderClass";

    private String orderNumber;
    private String name;        // name of person who ordered
    private String email;       // email of person ordering??
    private Pair<String, Float> addOn = new Pair("", 0f);      // name and price of the add on
    private Float totalPrice;
    private Date date;


    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefSpecials;


    // pick 1 depending if orders support multiple drinks
    // FOR NOW ASSUME ONLY ONE DRINK PER ORDER
    private ArrayList<Drink> drinks;
    private Drink drink;

    public Order() {

    }

    public Order(String orderNumber, String name, String email, Drink drink) {
        this.orderNumber = orderNumber;
        this.name = name;
        this.email = email;
        this.drink = drink;
        this.totalPrice = 0f;
        this.date = new Date();
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Float getTotalPrice() {
        return drink.getPrice() + addOn.second;
    }

    public Drink getDrink() {
        return drink;
    }

    public Date getDate() {
        return date;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTotalPrice(Float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void setAddOn(String name, Float price) {
        addOn = new Pair(name, price);
    }

    public void setDrink(Drink drink) {
        this.drink = drink;
    }

    @Override
    public int compareTo(Order o) {
        return getDate().compareTo(o.getDate());
    }

    @Override
    public String toString() {
        return  getName()+"  "+getDrink();
    }

    /**
     * grabs the loadout from the database and makes it
     */
    public void acquireLoadout(final Order order, final View view) {
        Log.e(TAG, "Starting: acquireLoadout");

        final ArrayList<Loadout> loadout = new ArrayList<>();

        // connect to database
        mRefSpecials = mDatabase.getReference("loadout");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for(DataSnapshot data: dataSnapshot.getChildren()){

                    // for each element in the loadout in the database grab it's
                    // key (drinkName) and value (amountLeft) and add it to a list
                    Loadout newLoadout = data.getValue(Loadout.class);
                    loadout.add(newLoadout);
                    Log.e(TAG, "loadout: "+newLoadout.toString());
                }
                checkDrinkOrder(order, loadout, view);
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
    private void checkDrinkOrder(Order order, ArrayList<Loadout> loadouts, final View view) {
        Log.e(TAG, "Starting: checkDrinkOrder");
        boolean makeDrink = true;
        for(String key: order.getDrink().getIngredients().keySet()) {
            Float amount = order.getDrink().getIngredients().get(key);

            boolean check = false;
            for(Loadout loadout: loadouts) {
                Log.e(TAG, "-"+loadout.getBottleName()+"-");
                // check to make sure "ingredient" is in the loadout and there is enough of it to make a drink
                if(loadout.getBottleName().equals(key) && loadout.getAmountLeft() >= amount) {
                    check = true;
                }
            }

            // got through all elements of loadout for specific ingredient
            // if check is false, then ingredient is not in loadout -> can't make the drink
            if(!check) {
                Log.e(TAG, "        checkDrinkOrder -> failed -"+key+"-");
                Toast.makeText(view.getContext(), "Drink order could not be made. Check loadout", Toast.LENGTH_SHORT).show();
                makeDrink = false;
                break;
            }
        }

        // if got all the way through without changing "makeDrink" to false
        // means all the ingredients are in the loadout, so make the drink
        if(makeDrink) {
            orderDrink(order, view);
        }
    }

    /**
     * This will interface with the database to...
     *      - make an order (being kept in the database)
     *      - order the drink (throw it in the drink queue)
     *          - take away x amount of ingredient from database (loadout)
     *
     * @param order - order to be processed
     */
    private void orderDrink(final Order order, final View view) {
        // now have to actually interface with the database

        Log.e(TAG, "Starting: orderDrink");

        // connect to database
        mRefSpecials = mDatabase.getReference("loadout");
        mRefSpecials.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String command = "";

                // for each ingredient in an order
                for(String ingKey: order.getDrink().getIngredients().keySet()) {
                    // grab the amount
                    Float ingAmount = order.getDrink().getIngredients().get(ingKey);

                    // read the loadout from the database
                    for(DataSnapshot loadoutEntry: dataSnapshot.getChildren()){

                        Loadout loadEnt = loadoutEntry.getValue(Loadout.class);
                        if (ingKey.equals(loadEnt.getBottleName())) {
                            // take away ingredient amount from table in database
                            Long oldValue = loadEnt.getAmountLeft();
                            Float newValue = oldValue - ingAmount;
//                            Float newValue = value - ingred.getAmount();

                            if (command.equals("")) {
                                command = loadoutEntry.getKey() + "_" + Float.toString(ingAmount);
                            } else {
                                command += "+" + loadoutEntry.getKey() + "_" + Float.toString(ingAmount);
                            }

                            // set the new value to the database (loadout table)
                            mDatabase.getReference("loadout").child(loadoutEntry.getKey()).child("amountLeft").setValue(newValue);
                        }
                    }
                }

                // when it gets here it means all the backend stuff is done, so throw it on the drinkQueue
                DatabaseReference orderRef = mDatabase.getReference("order");
                Toast.makeText(view.getContext(), "Order Added", Toast.LENGTH_SHORT).show();

                // grab the key from the database
                order.setOrderNumber(orderRef.push().getKey());
                // make the data object (Order) in the database at that key
                orderRef.child(order.getOrderNumber()).setValue(order);

                mDatabase.getReference().child("queue").child(order.getOrderNumber()).setValue(command);

                Log.e(TAG, "order completed");
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }


    public void removeOrder(String orderNumber) {
        mDatabase.getReference().child("queue").child(orderNumber).removeValue();
        mDatabase.getReference().child("order").child(orderNumber).removeValue();
    }


    public void makeCommand(Order order) {

        // variable for command to send to pie
        String command = "";

        HashMap<String, Float> ingredients = order.getDrink().getIngredients();

        // for each ingredient in order, put key value pairs into command -> string delimited
        for(String ingredient: order.getDrink().getIngredients().keySet()) {
            Log.e(TAG, "Ingredient: "+ingredient);

            Float amount = ingredients.get(ingredient);

            if(command.equals("")) {
                command = ingredient+"_"+Float.toString(amount);
            } else {
                command += "+"+ingredient+"_"+Float.toString(amount);
            }
        }



        // make put the key in the queue for reference
    }
}
