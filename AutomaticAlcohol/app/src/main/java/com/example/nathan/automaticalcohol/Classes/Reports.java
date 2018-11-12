package com.example.nathan.automaticalcohol.Classes;

import android.util.Log;
import android.util.Pair;

import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class Reports {

    private final static String TAG = "Reports Class";

    private List<Drink> mOrderList = new ArrayList<>();
    private HashMap<String, Integer> drinkCount = new HashMap<>();
    private HashMap<String, Drink> drinkName = new HashMap<>();
    private Set<Drink> nameSet = new HashSet<>();


    public List<Drink> getMost(DataSnapshot dataSnapshot) {

        // count the number of drinks in orders
        for(DataSnapshot data: dataSnapshot.getChildren()) {

            // TODO: make this check dates

            // grab the drink info
            Log.e(TAG, data.getValue(Order.class).toString());
            Order order = data.getValue(Order.class);
            Drink drink = order.getDrink();

            // throw the drink in the set
            nameSet.add(drink);
            drinkName.put(drink.getName(), drink);

            // if not in drinkCount already
            if(drinkCount.containsKey(drink.getName())) {
                drinkCount.put(drink.getName(), drinkCount.get(drink.getName()) + 1);
            } else {
                drinkCount.put(drink.getName(), 1);
            }
        }

        Log.e("check length", String.valueOf(drinkCount.size()));
        Log.e("check entries", String.valueOf(drinkCount));

        int iter = 0;
        while(!drinkCount.isEmpty() || iter > 5) {
            iter++;
            Log.e("length of hash", String.valueOf(iter));
            Integer greatest = -1;
            String bestDrink = null;
            for(Map.Entry<String,Integer> entry : drinkCount.entrySet()) {
                if(entry.getValue() > greatest) {
                    greatest = entry.getValue();
                    bestDrink = entry.getKey();
                }
            }
            drinkCount.remove(bestDrink);
            mOrderList.add(drinkName.get(bestDrink));
        }

        for(Drink entry: mOrderList) {
            Log.e("END PRINT", entry.getName());
        }

        return mOrderList;
    }


}
