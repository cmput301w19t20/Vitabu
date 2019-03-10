package com.example.vitabu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private String logTag = "MainActivity";
    public static final String EXTRA_MESSAGE = "IntentJson";
    private LocalUser localUser;
    private FirebaseUser firebaseUser;
    static public LocalUser t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Initialize firebase auth.
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
    }

    @Override
    protected void onStart(){
        super.onStart();
        // Check if already signed in.
        //if (firebaseUser != null) {
        //    Log.i(logTag, "Signed in as: " + firebaseUser.getDisplayName());
        //    updateUI();
        //}
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




    /**
     * Sign in user with firebase.
     */
    public void signIn(final String email, final String password) {
        // Attempt to sign in a user with email.
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(logTag, "Successfully signed in with email: " + email);
                            firebaseUser = auth.getCurrentUser();
                            localUser = new LocalUser();
                            t = localUser;
                            updateUI();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(logTag, "Failed to sign in with email: " + email, task.getException());
                            Toast.makeText(MainActivity.this, "Sign In failed.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI();
                        }
                    }
                });
    }



    /**
     * Signs a firebase user out.
     */
    public void signOut(){
        FirebaseUser usr = auth.getCurrentUser();
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
                        updateUI();
                    }
                });
    }


    public void onPressLogin(View view) {
//        Intent intent = new Intent(this, browseBooksActivity.class);
//        startActivity(intent);
        // TODO: Validate login details. If valid email/password combo, proceed, otherwise alert user to incorrect login.
        String email = ((TextView) findViewById(R.id.login_email)).getText().toString();
        String password = ((TextView) findViewById(R.id.login_password)).getText().toString();
        if (email.length() < 1 || password.length() < 1){
            Toast.makeText(MainActivity.this, "The email and password fields cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }
        signIn(email, password);

    }

    public void onPressRegister(View view) {
        // Start register activity.
        Intent intent = new Intent(this, registerActivity.class);
        startActivity(intent);
    }

    public void updateUI(){
        if (firebaseUser == null) {
            // No user signed in.
            Log.d(logTag, "Update ui: No user signed in.");
            return;
        }


        // TODO Update ui here with newly signed in users info.
        String userName = firebaseUser.getDisplayName();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(userName).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        localUser = (dataSnapshot.getValue(LocalUser.class));
                        t = localUser;
                        Log.d(logTag, "Read owner");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.d(logTag, "Cancelled");
                    }
                }
        );

        IntentJson passing = new IntentJson(localUser);
        Intent intent = new Intent(this, browseBooksActivity.class);
        intent.putExtra(EXTRA_MESSAGE, passing.toJson());
        startActivity(intent);

    }

}
