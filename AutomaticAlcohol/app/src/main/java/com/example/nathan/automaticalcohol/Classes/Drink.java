package com.example.nathan.automaticalcohol.Classes;

import java.util.ArrayList;

public class Drink {
    private String name;
    private String description;
    private String image;
    private ArrayList<Ingredient> ingredients;
    private Float price;

    public Drink() {
        // default constructor
    }

    public Drink(String name, String description, String image, ArrayList<Ingredient> ingredients, Float price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.ingredients = ingredients;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public Float getPrice() {
        return price;
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String makeString() {
        return this.description+"  "+this.price;
    }
}
