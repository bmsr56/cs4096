package com.example.nathan.automaticalcohol.Activities;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.BluetoothConnect;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String EXTRA_MESSAGE = "com.example.automaticalcohol.MESSAGE";

    private BluetoothAdapter mBluetoothAdapter;

    private EditText email;
    private EditText password;

    private BluetoothConnect bluetoothConnect;



    private FirebaseAuth mAuth;

    private GoogleSignInClient mGoogleSignInClient;




    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_WRITE:
                    Toast.makeText(MainActivity.this, "Message sent", Toast.LENGTH_SHORT).show();
                    break;
                case Constants.MESSAGE_READ:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);

                    if ("accept".equals(writeMessage.substring(0, msg.arg1))) {
                        Intent intent = new Intent(MainActivity.this, BartenderActivity.class);
                        startActivity(intent);
                        bluetoothConnect.close();
                    } else if ("bartender".equals(writeMessage.substring(0, msg.arg1))) {
                        Intent intent = new Intent(MainActivity.this, BartenderActivity.class);
                        startActivity(intent);
                        bluetoothConnect.close();
                    } else if ("user".equals(writeMessage.substring(0, msg.arg1))) {
                        Intent intent = new Intent(MainActivity.this, UserActivity.class);
                        startActivity(intent);
                        bluetoothConnect.close();

                    } else {
                        Toast.makeText(MainActivity.this, "Incorrect username or password", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // initialize firebase instance
        mAuth = FirebaseAuth.getInstance();

        // init email/pass views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        Button login_button = findViewById(R.id.email_sign_in_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                String user_pass = password.getText().toString();


                GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .build();

                // Build a GoogleSignInClient with the options specified by gso.
                mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

                mAuth.signInWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.e(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });


            }
        });

        Button new_account = findViewById(R.id.new_account_button);
        new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                String user_pass = password.getText().toString();

                mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });


            }
        });


        Button google_signin = findViewById(R.id.sign_in_button);
        google_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_email = email.getText().toString();
                String user_pass = password.getText().toString();

                mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });


            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        updateUI(account);
    }


    // TODO: figure out which one of these is to be used... unless we need both...
    private void updateUI(FirebaseUser user) {

    }

    private void updateUI(GoogleSignInAccount account) {

    }

    private void sendMessage(String message) {
        if (message.length() > 0) {
            byte[] data = message.getBytes();
            bluetoothConnect.write(data);
        }
    }

    private void connectDevice() {
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice("B8:27:EB:C7:30:39");
        bluetoothConnect.connect(device);
    }




}
