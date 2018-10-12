package com.example.nathan.automaticalcohol.Classes;

public class Loadout {
    private String amountLeft;
    private String bottleName;

    public Loadout() {

    }

    public Loadout(String amountLeft, String bottleName) {
        this.amountLeft = amountLeft;
        this.bottleName = bottleName;
    }

    public String getAmountLeft() {
        return amountLeft;
    }

    public String getBottleName() {
        return bottleName;
    }
}
