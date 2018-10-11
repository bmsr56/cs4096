package com.example.nathan.automaticalcohol.Classes;

public class Color {
    private String category;
    private String color;
    private String type;

    public Color() {

    }

    public Color(String category, String color, String type) {
        this.category = category;
        this.color = color;
        this.type = type;
    }

    public String getCategory() {
        return category;
    }

    public String getColor() {
        return color;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        String result = getCategory() + ", " + getColor() + ", " + getType();
        return result;
    }
}
