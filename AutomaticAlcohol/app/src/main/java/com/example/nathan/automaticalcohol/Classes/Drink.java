package com.example.nathan.automaticalcohol.Classes;

<<<<<<< HEAD
import java.util.Arrays;
import java.util.HashMap;
=======
import java.util.ArrayList;
>>>>>>> added some class files, started to update accordingly

public class Drink {
    private String name;
    private String description;
    private String image;
<<<<<<< HEAD
    private HashMap<String, Float> ingredients;
    private Float price;
=======
    private ArrayList<Ingredient> ingredients;
    private String price;
>>>>>>> added some class files, started to update accordingly

    public Drink() {
        // default constructor
    }

<<<<<<< HEAD
    public Drink(String name, String description, String image, HashMap<String, Float> ingredients, Float price) {
//    public Drink(String name, String description, String image, Float price) {
        this.name = name;
=======
    public Drink(String description, String image, ArrayList<Ingredient> ingredients, String price) {
>>>>>>> added some class files, started to update accordingly
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

    public String getPrice() {
        return price;
    }

<<<<<<< HEAD
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
=======
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public String makeString() {
        return this.description+"  "+this.price;
>>>>>>> added some class files, started to update accordingly
    }
}
