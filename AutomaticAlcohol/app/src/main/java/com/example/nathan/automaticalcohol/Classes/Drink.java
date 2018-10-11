package com.example.nathan.automaticalcohol.Classes;

public class Drink {
    private String description;
    private String image;
    private Float price;

    public Drink() {
        // default constructor
    }

    public Drink(String description, String image, Float price) {
        this.description = description;
        this.image = image;
        this.price = price;
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


}
