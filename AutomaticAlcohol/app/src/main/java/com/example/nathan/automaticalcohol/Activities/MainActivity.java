package com.example.nathan.automaticalcohol.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.automaticalcohol.MESSAGE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final AutoCompleteTextView email = findViewById(R.id.email);
        final EditText password = findViewById(R.id.password);
        Button login_button = findViewById(R.id.email_sign_in_button);

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email_text = email.getText().toString();
                String password_text = password.getText().toString();


                // get info from database if user is bartender/user

                if(email_text.equals("bartender")) {
                    Intent intent = new Intent(v.getContext(), BartenderActivity.class);
                    startActivity(intent);
                }
                else if(email_text.equals("user")) {
                    Intent intent = new Intent(v.getContext(), UserActivity.class);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(v.getContext(), "Ya Done Fucked Up, Bitch....", Toast.LENGTH_LONG).show();
                }
            }
        });


    }
}
