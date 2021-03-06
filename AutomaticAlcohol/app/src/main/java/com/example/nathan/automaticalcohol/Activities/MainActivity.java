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

import com.example.nathan.automaticalcohol.Classes.User;
import com.example.nathan.automaticalcohol.Constants;
import com.example.nathan.automaticalcohol.R;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.FirebaseError;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";

    private EditText email;
    private EditText password;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mBartenderRef;
    private DatabaseReference mUserRef;

    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // initialize firebase instance
        mAuth = FirebaseAuth.getInstance();

        // init database instance
        mDatabase = FirebaseDatabase.getInstance();
        // init database references
        mBartenderRef = mDatabase.getReference("bartenders");
        mUserRef = mDatabase.getReference("accounts");


        // init email/pass views
        email = findViewById(R.id.email);
        email.setText("reorh6@mst.edu");
        password = findViewById(R.id.password);
        password.setText("password");

        Button login_button = findViewById(R.id.email_sign_in_button);
        login_button.setOnClickListener(mSignInListener);

        Button new_account = findViewById(R.id.new_account_button);
        new_account.setOnClickListener(mSignInListener);


        // Google Sign In init
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);

        // grab the google button
        SignInButton signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, Constants.RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null) {
            Log.e(TAG, account.getDisplayName());
        } else {
            Log.e(TAG, "ACCOUNT IS NULL");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == Constants.RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            Log.e(TAG, "Account signed in");
            // Signed in successfully, show authenticated UI.
            updateUI(account);
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.e(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    /**
     * Logs in a Google account
     * @param account
     */
    // for now assumes all Google accounts are users and not bartenders
    private void updateUI(GoogleSignInAccount account) {
        Log.e(TAG, "updateUI (user) -> " + account.getEmail());
        Intent intent = new Intent(MainActivity.this, UserActivity.class);
        startActivity(intent);
    }

    /**
     *    Operates under the assumption that if they were able to get this far they have to be an
     * authenticated email address. This is true because in the 'onClick' portion of
     * 'View.OnClickListener mSignInListener' where the switchc-case is 'email_sign_in_button' it
     * checks whether or not the user has been authenticated (signed in before)
     *
     *    So this part grabs their info and checks if they are a bartender, as that is the smaller
     * list and therefore faster. If they are a bartender it sends them to the 'PinActivity'
     * otherwise it sends them to the 'UserActivity'
     *
     * @param user
     */
    private void loginUser(final FirebaseUser user) {

        // if they aren't in the "bartender" table and made it this far they have to be a user in "accounts" table
        mBartenderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            boolean check = false;
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    // checks if 'user' is a bartender
                    if (data.getKey().equals(user.getUid())) {
                        Log.e(TAG, "account is a bartender");
                        Intent intent = new Intent(MainActivity.this, PinActivity.class);
                        startActivity(intent);
                        check = true;
                        break;
                    }
                }
                if (!check) {
                    // this means they are a regular user
                    Log.e(TAG, "account is a user");
                    Intent intent = new Intent(MainActivity.this, UserActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(DatabaseError firebaseError) {

            }
        });
    }

    /**
     * The user is now an authenticated user within the Firebase app, but is not recorded in our
     * database. This records them in our database.
     *
     * @param user
     */
    private void addUser(final FirebaseUser user) {
        String uid = user.getUid();
        mUserRef.child(uid).setValue(new User(user.getEmail()));
    }

        /**
         * Handles what happens when clickables that are liked with this listener are clicked
         */
    private View.OnClickListener mSignInListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();

            // TODO: check that these are valid email/password
            String user_email = email.getText().toString();
            String user_pass = password.getText().toString();
            
            if (user_email.length() == 0 || user_pass.length() == 0) {
                Toast.makeText(MainActivity.this, "Email and password are needed", Toast.LENGTH_SHORT).show();
                return;
            }

            switch (id) {
                case R.id.email_sign_in_button:
                    mAuth.signInWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.e(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                loginUser(user);
                            } else {
                                // If sign in fails, display a message to the user.

                                // TODO: might check if the email has been used so far -> may not be just the password that's incorrect

                                switch (task.getException().getMessage()) {
                                    case Constants.INVALID_EMAIL:
                                        Log.e(TAG, "createUserWithEmail:failure - invalid email");
                                        Toast.makeText(MainActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Constants.EMAIL_ALREADY_USED:
                                        Log.e(TAG, "createUserWithEmail:failure - email used");
                                        Toast.makeText(MainActivity.this, "Email has already been used or the password is incorrect", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Constants.WEAK_PASS:
                                        Log.e(TAG, "createUserWithEmail:failure - bad pass");
                                        Toast.makeText(MainActivity.this, "Password needs at least 6 characters", Toast.LENGTH_SHORT).show();
                                        break;
                                    default:
                                        Log.e(TAG, "createUserWithEmail:failure - wrong pass");
                                        Toast.makeText(MainActivity.this, "Incorrect password", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    });
                    break;

                case R.id.new_account_button:
                    mAuth.createUserWithEmailAndPassword(user_email, user_pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                addUser(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                switch (task.getException().getMessage()) {
                                    case Constants.INVALID_EMAIL:
                                        Log.e(TAG, "createUserWithEmail:failure - invalid email");
                                        Toast.makeText(MainActivity.this, "Invalid email", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Constants.EMAIL_ALREADY_USED:
                                        Log.e(TAG, "createUserWithEmail:failure - email used");
                                        Toast.makeText(MainActivity.this, "Email has already been used", Toast.LENGTH_SHORT).show();
                                        break;
                                    case Constants.WEAK_PASS:
                                        Log.e(TAG, "createUserWithEmail:failure - bad pass");
                                        Toast.makeText(MainActivity.this, "Password needs at least 6 characters", Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        }
                    });

                break;
            }
        }
    };
}
