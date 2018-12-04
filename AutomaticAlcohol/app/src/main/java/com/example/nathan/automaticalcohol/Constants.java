package com.example.nathan.automaticalcohol;

public interface Constants {

    int MESSAGE_READ = 0;
    int MESSAGE_WRITE = 1;
    int MESSAGE_TOAST = 2;

    int REQUEST_ENABLE_BT = 1;

    int MIN_BOTTLE = 1;
    int MAX_BOTTLE = 6;

    int RC_SIGN_IN = 20;


    String INVALID_EMAIL = "The email address is badly formatted.";
    String EMAIL_ALREADY_USED = "The email address is already in use by another account.";
    String WEAK_PASS = "The given password is invalid. [ Password should be at least 6 characters ]";

    String DRINK_QUEUE = "drinkQueue";
    String SPECIALS = "specials";
    String TABS = "tabs";
    String QUICK = "quick";
    String DRINKS = "lottaDrinks";

    String PIN_TO_BARTENDER_PIN = "fromPinToBartender";
    String BARTENDER_TO_HOME_TAB_PIN = "fromBartenderToFragment";
}



