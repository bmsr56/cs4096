package com.example.nathan.automaticalcohol.Classes;

public class Ingredient {
    private String name;
<<<<<<< HEAD
    private Float amount;
=======
    private String amount;
>>>>>>> added some class files, started to update accordingly

    public Ingredient() {

    }
<<<<<<< HEAD
    public Ingredient(String name, Float amount) {
=======
    public Ingredient(String name, String amount) {
>>>>>>> added some class files, started to update accordingly
        this.name = name;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

<<<<<<< HEAD
    public Float getAmount() {
        return amount;
    }

=======
>>>>>>> added some class files, started to update accordingly
    public void setName(String name) {
        this.name = name;
    }

<<<<<<< HEAD
    public void setAmount(Float amount) {
=======
    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
>>>>>>> added some class files, started to update accordingly
        this.amount = amount;
    }
}
