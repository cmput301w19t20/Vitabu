package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Arrays;
import java.util.List;

/**
 * @author Tristan Carlson
 * @version 0.1
 *
 * Activity to Authenticate users.
 */
public class AuthActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private int RC_SIGN_IN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);
        auth = FirebaseAuth.getInstance();
        // Set up buttons.
        Button signInButton = findViewById(R.id.sign_in_button);
        Button signOutButton= findViewById(R.id.sign_out_button);
        signInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signIn();
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                signOut();
            }
        });

    }

    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser user = auth.getCurrentUser();
        // Check if already signed in.
        if (user != null) {
            Log.i("AuthActivity", "Signed in as: " + user.toString());
            // TODO Update app with user info here
            //this.finish();
        }
    }

    /**
     * Called after startActivityForResult returns.  Used to get result from other activity.
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN){
            IdpResponse resp = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                Log.i("AuthActivity", "Sucsessfully signed in :" + user.toString());
                /* TODO Implement username customizability.
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(USERNAME).build();

                user.updateProfile(profileUpdates);
                */
                this.finish();
            }
            else{
                Log.i("AuthActivity",  "Sign in error : " + Integer.toString(resp.getError().getErrorCode()));
            }
        }
    }

    /**
     * Uses Firebase ui to sign a user in.
     */
    public void signIn(){
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build());
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(providers)
                        .setIsSmartLockEnabled(false)
                        .build(),
                RC_SIGN_IN);


    }

    /**
     * Signs a user out.
     */
    public void signOut(){
        FirebaseUser usr = FirebaseAuth.getInstance().getCurrentUser();
        if (usr == null){
            return;
        }
        Log.i("AuthActivity", "Signing out user : " + usr.toString());
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.i("AuthActivity", "Signed out user successfully!");
                    }
                });
    }


}
