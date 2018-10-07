package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;

public class TabHomeFragment extends Fragment{
    private static final String TAG = "TabHomeFragment";

    private EditText editText_custName;
    private EditText editText_drinkName;
    private Spinner spinner_addItem;
    private TextView textView_totalCost;
    private Button buttonSubmitOrder;


    private Button button_special1;
    private Button button_special2;
    private Button button_special3;
    private Button button_special4;
    private Button button_special5;

    private Button button_quick1;
    private Button button_quick2;
    private Button button_quick3;
    private Button button_quick4;
    private Button button_quick5;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_home, container, false);

//        editText_custName = view.findViewById(R.id.editText_custName);
//        editText_drinkName = view.findViewById(R.id.editText_drinkName);
        // goes here when order button is clicked
//        buttonSubmitOrder = view.findViewById(R.id.buttonSubmitOrder);
//        buttonSubmitOrder.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button submit order", Toast.LENGTH_SHORT).show();
//
//                String custName = editText_custName.getText().toString();
//                String drinkName = editText_drinkName.getText().toString();
//                String addItem = spinner_addItem.getSelectedItem().toString();
//
//                textView_totalCost.setText(computeCost(addItem));
//            }
//        });


        // logic for handling specials
//        button_special1 = view.findViewById(R.id.button_special1);
//        button_special2 = view.findViewById(R.id.button_special2);
//        button_special3 = view.findViewById(R.id.button_special3);
//        button_special4 = view.findViewById(R.id.button_special4);
//        button_special5 = view.findViewById(R.id.button_special5);

        // each of these calls a function that orders a drink based on the name of the special
//        button_special1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button special 1", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special1.getText().toString());
//            }
//        });
//        button_special2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button special 2", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special2.getText().toString());
//            }
//        });
//        button_special3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button special 3", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special3.getText().toString());
//            }
//        });
//        button_special4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button special 4", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special4.getText().toString());
//            }
//        });
//        button_special5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button special 5", Toast.LENGTH_SHORT).show();
//                orderDrink(button_special5.getText().toString());
//            }
//        });


        // logic for handling quick add drinks
//        button_quick1 = view.findViewById(R.id.button_quick1);
//        button_quick2 = view.findViewById(R.id.button_quick2);
//        button_quick3 = view.findViewById(R.id.button_quick3);
//        button_quick4 = view.findViewById(R.id.button_quick4);
//        button_quick5 = view.findViewById(R.id.button_quick5);

        // each of these calls a function that orders a drink based on the name of the special
//        button_quick1.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button quick 1", Toast.LENGTH_SHORT).show();
//                orderDrink(button_quick1.getText().toString());
//            }
//        });
//        button_quick2.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button quick 2", Toast.LENGTH_SHORT).show();
//                orderDrink(button_quick2.getText().toString());
//            }
//        });
//        button_quick3.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button quick 3", Toast.LENGTH_SHORT).show();
//                orderDrink(button_quick3.getText().toString());
//            }
//        });
//        button_quick4.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button quick 4", Toast.LENGTH_SHORT).show();
//                orderDrink(button_quick4.getText().toString());
//            }
//        });
//        button_quick5.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                Toast.makeText(getActivity(), "Testing button quick 5", Toast.LENGTH_SHORT).show();
//                orderDrink(button_quick5.getText().toString());
//            }
//        });

















        return view;
    }

    private int computeCost(String addItem) {
        return 0;
    }

    private void orderDrink(String drink) {

    }
}
