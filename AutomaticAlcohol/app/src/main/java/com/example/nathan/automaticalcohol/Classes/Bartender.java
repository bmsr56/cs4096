package com.example.nathan.automaticalcohol.Classes;

import java.util.ArrayList;

public class Bartender {
    private String name;
    private ArrayList<Drink> specials;
    private String pin;

    public Bartender(String name, ArrayList<Drink> specials, String pin) {
        this.name = name;
        this.specials = specials;
        this.pin = pin;
    }

    public Bartender() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Drink> getSpecials() {
        return specials;
    }

    public void setSpecials(ArrayList<Drink> specials) {
        this.specials = specials;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }
}
