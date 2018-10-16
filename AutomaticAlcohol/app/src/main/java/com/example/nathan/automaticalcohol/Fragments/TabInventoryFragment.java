package com.example.nathan.automaticalcohol.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.Classes.Loadout;
import com.example.nathan.automaticalcohol.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TabInventoryFragment extends Fragment{
    private static final String TAG = "TabInventoryFragment";

    private EditText et_amountLeft;

    // TODO: is this the position of the bottle??   -> bottleLocation
    private EditText et_bottleNumber;
    private EditText et_bottleName;

    private Button btn_bottleChange;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mLoadoutReference;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab_inventory, container, false);

        // init Firebase instance
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();

        mLoadoutReference = mDatabase.getReference("loadout");

        et_amountLeft = view.findViewById(R.id.editText_amountLeft);
        et_bottleNumber = view.findViewById(R.id.editText_bottleNumber);
        et_bottleName = view.findViewById(R.id.editText_bottleName);

        btn_bottleChange = view.findViewById(R.id.button_bottleChange);
        btn_bottleChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountLeft = et_amountLeft.getText().toString();
                int bottleNumber = Integer.parseInt(et_bottleNumber.getText().toString());
                String bottleName = et_bottleName.getText().toString();

                // make sure number entered is a valid loadout position
                if (bottleNumber < 1 || bottleNumber > 6) {
                    Toast.makeText(getActivity(), "Enter valid loadout location", Toast.LENGTH_SHORT).show();
                } else {
                    // overwrite bottle position with new loadout
                    mLoadoutReference.child(Integer.toString(bottleNumber)).setValue(new Loadout(amountLeft, bottleName));
                }
            }
        });


        // TODO: make data change listener thing for bottle amounts
        // TODO: or should it just look once?...   above is more robust



        // TODO: connect data grabbed from database to graphs




        return view;
    }
}
