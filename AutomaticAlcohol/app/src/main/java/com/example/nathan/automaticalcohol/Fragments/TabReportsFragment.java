package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.nathan.automaticalcohol.R;

public class TabReportsFragment extends Fragment{
    private static final String TAG = "TabReportsFragment";

    private TextView textView_reports;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_reports, container, false);
        textView_reports = view.findViewById(R.id.textView_reports);
        textView_reports.setHorizontallyScrolling(true);
        textView_reports.setHorizontalScrollBarEnabled(true);

        StringBuilder s = new StringBuilder(
                "[15:37 11/27/18] Bartender: Stacy, Customer:  John, Drink: Highball(sprite:462ml,whiskey:44ml)" +
                "\n[15:31 11/27/18] Bartender: Stacy, Customer:   Jim, Drink: Cup O' Vodka(vodka:44ml)" +
                "\n[15:16 11/27/18] Bartender:   Sam, Customer:  Jack, Drink: Highball(sprite:462ml,whiskey:44ml)" +
                "\n[15:07 11/27/18] Bartender: Sarah, Customer: Jenny, Drink: Manhattan(vermouth:42ml,whiskey:44ml)" +
                "\n[15:00 11/27/18] Bartender:  Skye, Customer: Jamie, Drink: Rum and Coke(coke:462ml,rum:44ml)" +
                "\n[14:57 11/27/18] Bartender: Scott, Customer:  John, Drink: Screwdriver(orange juice:42ml,vodka:44ml)" +
                "\n[14:43 11/27/18] Bartender: Scott, Customer:   Jim, Drink: Rum and Coke(coke:462ml,rum:44ml)" +
                "\n[14:32 11/27/18] Bartender: Stacy, Customer: Jamie, Drink: Highball(sprite:462ml,whiskey:44ml)" +
                "\n[14:15 11/27/18] Bartender: Sarah, Customer:   Jim, Drink: Highball(sprite:462ml,whiskey:44ml)" +
                "\n[14:09 11/27/18] Bartender:  Skye, Customer: Jenny, Drink: Screwdriver(orange juice:42ml,vodka:44ml)" +
                "\n[13:46 11/27/18] Bartender:   Sam, Customer: Jamie, Drink: Rum and Coke(coke:462ml,rum:44ml)" +
                "\n[13:29 11/27/18] Bartender: Sarah, Customer:  John, Drink: Cup O' Vodka(vodka:44ml)" +
                "\n[13:15 11/27/18] Bartender: Sarah, Customer:  Jack, Drink: Manhattan(vermouth:42ml,whiskey:44ml)" +
                "\n[13:03 11/27/18] Bartender: Stacy, Customer:  Jack, Drink: Manhattan(vermouth:42ml,whiskey:44ml)" +
                "\n[12:26 11/27/18] Bartender:   Sam, Customer:   Jim, Drink: Screwdriver(orange juice:42ml,vodka:44ml)" +
                "\n[12:22 11/27/18] Bartender: Sarah, Customer: Jamie, Drink: Rum and Coke(coke:462ml,rum:44ml)" +
                "\n[12:17 11/27/18] Bartender: Scott, Customer:  John, Drink: Screwdriver(orange juice:42ml,vodka:44ml)" +
                "\n[12:15 11/27/18] Bartender:  Skye, Customer: Jenny, Drink: Highball(sprite:462ml,whiskey:44ml)" +
                "\n[12:17 11/27/18] Bartender: Sarah, Customer:  John, Drink: Manhattan(vermouth:42ml,whiskey:44ml)" +
                "\n[11:57 11/27/18] Bartender: Scott, Customer:  Jack, Drink: Cup O' Vodka(vodka:44ml)" +
                "\n[11:52 11/27/18] Bartender: Scott, Customer:   Jim, Drink: Rum and Coke(coke:462ml,rum:44ml)" +
                "\n[11:48 11/27/18] Bartender:   Sam, Customer:   Jim, Drink: Screwdriver(orange juice:42ml,vodka:44ml)" +
                "");
        textView_reports.setText(s);
        return view;
    }
}
