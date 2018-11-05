package com.example.nathan.automaticalcohol.Classes;

import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;


public class Order implements Comparable<Order>{
    private String orderNumber;
    private String name;        // name of person who ordered
    private String email;       // email of person ordering??
    private Pair<String, Float> addOn = new Pair("", 0f);      // name and price of the add on
    private Float totalPrice;
    private Date date;

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
}
