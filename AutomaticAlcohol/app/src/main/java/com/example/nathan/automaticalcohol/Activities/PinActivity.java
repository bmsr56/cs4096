package com.example.nathan.automaticalcohol.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class PinActivity extends AppCompatActivity {

    private final String TAG = "PinActivity";
    private final String EXTRA_PASS_PIN = "PinActivity_passPin";


    private FirebaseAuth mAuth;
    private DatabaseReference mBartenderRef;
    private FirebaseDatabase mDatabase;

    private String input = "";

    private Button button_login1;
    private Button button_login2;
    private Button button_login3;
    private Button button_login4;
    private Button button_login5;
    private Button button_login6;
    private Button button_login7;
    private Button button_login8;
    private Button button_login9;
    private Button button_loginEnter;

    private TextView textView_pinInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartender_pin);

        // database connection / setup
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mBartenderRef = mDatabase.getReference("bartenders").child(mAuth.getUid());



        // defining buttons and what happens when you click them
        button_login1 = findViewById(R.id.button_login1);
        button_login1.setOnClickListener(mClickListener);
        button_login2 = findViewById(R.id.button_login2);
        button_login2.setOnClickListener(mClickListener);
        button_login3 = findViewById(R.id.button_login3);
        button_login3.setOnClickListener(mClickListener);
        button_login4 = findViewById(R.id.button_login4);
        button_login4.setOnClickListener(mClickListener);
        button_login5 = findViewById(R.id.button_login5);
        button_login5.setOnClickListener(mClickListener);
        button_login6 = findViewById(R.id.button_login6);
        button_login6.setOnClickListener(mClickListener);
        button_login7 = findViewById(R.id.button_login7);
        button_login7.setOnClickListener(mClickListener);
        button_login8 = findViewById(R.id.button_login8);
        button_login8.setOnClickListener(mClickListener);
        button_login9 = findViewById(R.id.button_login9);
        button_login9.setOnClickListener(mClickListener);

        button_loginEnter = findViewById(R.id.button_loginEnter);
        button_loginEnter.setOnClickListener(mClickListener);

        textView_pinInput = findViewById(R.id.textView_pinInput);
    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            switch (id) {
                case R.id.button_loginEnter:
                    // check if bartender pin when clicked enter
                    checkBartenderLogin(input);
                    break;

                // when number buttons clicked, remember it and add a '*' to screen
                case R.id.button_login1:
                    input = input+"1";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login2:
                    input = input+"2";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login3:
                    input = input+"3";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login4:
                    input = input+"4";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login5:
                    input = input+"5";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login6:
                    input = input+"6";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login7:
                    input = input+"7";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login8:
                    input = input+"8";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
                case R.id.button_login9:
                    input = input+"9";
                    textView_pinInput.setText(textView_pinInput.getText()+"*");
                    break;
            }
        }
    };

    /**
     * Checks if the input pin is the pin of a bartender. If so it opens a new activity
     *
     * @param pin
     */
    private void checkBartenderLogin(final String pin) {

        mBartenderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    Log.e(TAG, data.getKey());

                    // if the pin entered matches a pin in the database
                    if (data.getKey().equals(pin)) {
                        Log.e(TAG, "pin IS in database");

                        // reset the pin and what's on the screen
                        textView_pinInput.setText("");
                        input = "";

                        // start another page and pass the pin with it
                        Intent intent = new Intent(PinActivity.this, BartenderActivity.class);
                        intent.putExtra(EXTRA_PASS_PIN, pin);
                        startActivity(intent);
                        check = true;
                    }
                }
                if (!check) {
                    // means the pin was not in the database, so start over
                    Log.e(TAG, "pin not in database");
                    input = "";
                    textView_pinInput.setText("");
                    Toast.makeText(PinActivity.this, "Incorrect pin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
