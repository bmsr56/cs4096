package com.example.nathan.automaticalcohol.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.nathan.automaticalcohol.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class UserActivity extends AppCompatActivity {
    private static final String TAG = "UserActivity";


    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        // Google Sign In init (used for sign out purposes)
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(UserActivity.this, gso);
        Button signOut = findViewById(R.id.user_signout_btn);
        signOut.setOnClickListener(mUserActivityListener);

    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(UserActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        // might throw "logout successful" kind of a thing in here
                        Toast.makeText(UserActivity.this, "Logout successful", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private View.OnClickListener mUserActivityListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            switch (id) {
                case R.id.user_signout_btn:
                    Log.e(TAG, "button clicked");
                    signOut();
                    Intent intent = new Intent(UserActivity.this, MainActivity.class);
                    Log.e(TAG, "LOGGING OUT");
                    startActivity(intent);
                    break;
            }


        }
    };
}

