package com.example.nathan.automaticalcohol.Classes;

import java.util.ArrayList;

public class Order {
    private String orderNumber;
    private String name;   // name of person who ordered
    private String email;       // email of person ordering??
    private Float totalPrice;

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
        return totalPrice;
    }

    public Drink getDrink() {
        return drink;
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

    public void setDrink(Drink drink) {
        this.drink = drink;
    }
}
