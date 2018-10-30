package com.example.nathan.automaticalcohol.Classes;

public class Ingredient {
    private String name;
    private Float amount;

    public Ingredient() {

    }
    public Ingredient(String name, Float amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Float getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
