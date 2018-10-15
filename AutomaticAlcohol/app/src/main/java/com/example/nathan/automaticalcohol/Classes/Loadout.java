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

    public void setAmountLeft(String amountLeft) {
        this.amountLeft = amountLeft;
    }

    public void setBottleName(String bottleName) {
        this.bottleName = bottleName;
    }

    public String toString() {
        return this.amountLeft+" of "+this.bottleName;
    }
}

