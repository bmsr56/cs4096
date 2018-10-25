package com.example.nathan.automaticalcohol.Classes;

public class Loadout {
    private String bottleName;
    private Long amountLeft;

    public Loadout() {

    }

    public Loadout(String bottleName, Long amountLeft) {
        this.amountLeft = amountLeft;
        this.bottleName = bottleName;
    }

    public Long getAmountLeft() {
        return amountLeft;
    }

    public String getBottleName() {
        return bottleName;
    }

    public void setAmountLeft(Long amountLeft) {
        this.amountLeft = amountLeft;
    }

    public void setBottleName(String bottleName) {
        this.bottleName = bottleName;
    }

    public String toString() {
        return this.amountLeft+" of "+this.bottleName;
    }
}

