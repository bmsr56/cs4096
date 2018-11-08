package com.example.nathan.automaticalcohol;

import com.example.nathan.automaticalcohol.Classes.Drink;
import com.example.nathan.automaticalcohol.Classes.Order;

public interface RecyclerInterface {
    void onTagClicked(Drink tagName);
    void onTagClicked(Order order);
}

