package com.example.nathan.automaticalcohol.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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


    private EditText editText_pinInput;
    private Button btn_enterPin;

    private FirebaseAuth mAuth;
    private DatabaseReference mBartenderRef;
    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bartender_pin);

        editText_pinInput = findViewById(R.id.editText_pinInput);

        btn_enterPin = findViewById(R.id.button_enterPin);
        btn_enterPin.setOnClickListener(mClickListener);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mBartenderRef = mDatabase.getReference("bartenders").child(mAuth.getUid());


    }


    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            String pinInput = editText_pinInput.getText().toString();

            switch (id) {
                case R.id.button_enterPin:
                    checkBartenderLogin(pinInput);
                    break;
            }
        }
    };

    private void checkBartenderLogin(final String pin) {

        mBartenderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){

                    Log.e(TAG, data.getKey());

                    if (data.getKey().equals(pin)) {
                        Log.e(TAG, "pin IS in database");
                        // TODO: this is going to have to pass info to a the next intent

                        Intent intent = new Intent(PinActivity.this, BartenderActivity.class);
                        intent.putExtra(EXTRA_PASS_PIN, pin);
                        startActivity(intent);
                        check = true;
                    }
                }
                if (!check) {
                    // means the pin was not in the database
                    Log.e(TAG, "pin not in database");
                    Toast.makeText(PinActivity.this, "Incorrect pin", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

}
