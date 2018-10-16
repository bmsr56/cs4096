package com.example.nathan.automaticalcohol.Classes;

public class Ingredient {
    private String name;
    private Long amount;

    public Ingredient() {

    }
    public Ingredient(String name, Long amount) {
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public Long getAmount() {
        return amount;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }
}
