package com.example.nathan.automaticalcohol.Classes;

public class User {

    private String email;

    public User() {
        // default constructor
    }

    public User(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return email;
    }

}
