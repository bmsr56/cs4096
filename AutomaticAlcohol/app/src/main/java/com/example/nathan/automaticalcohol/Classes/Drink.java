package com.example.nathan.automaticalcohol.Classes;

import java.util.Arrays;
import java.util.HashMap;

public class Drink {
    private String name;
    private String description;
    private String image;
    private HashMap<String, Float> ingredients;
    private Float price;

    public Drink() {
        // default constructor
    }

    public Drink(String name, String description, String image, HashMap<String, Float> ingredients, Float price) {
//    public Drink(String name, String description, String image, Float price) {
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

    public HashMap<String, Float> getIngredients() {
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

    public void setIngredients(HashMap<String, Float> ingredients) {
        this.ingredients = ingredients;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String makeString() {

        return this.description+"  "+this.price+"  \n"+Arrays.asList(ingredients);
    }
}
