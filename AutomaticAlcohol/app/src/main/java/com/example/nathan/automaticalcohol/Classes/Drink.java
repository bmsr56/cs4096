package com.example.nathan.automaticalcohol.Classes;

import java.util.ArrayList;

public class Drink {
    private String description;
    private String image;
    private ArrayList<Ingredient> ingredients;
    private String price;

    public Drink() {
        // default constructor
    }

    public Drink(String description, String image, ArrayList<Ingredient> ingredients, String price) {
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String makeString() {
        return this.description+"  "+this.price;
    }
}
