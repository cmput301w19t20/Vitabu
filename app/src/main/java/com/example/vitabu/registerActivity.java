/*
 * This file deals with the functionality of registering a new user. It grabs all the input from
 * the user to fill out the model classes. It then checks if the username is taken, if the email
 * is already linked to some user and does not let a person register in that case.
 *
 * Author: Arseniy Kouzmenkov
 *
 * Version 1.1
 *
 * Outstanding issues: Migrating old users to fit the database requirements.
 *
 */
package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONObject;

/**
 * This class deals with everything pertaining to new user registration to Vitabu.
 *
 * @author Arseniy Kouzmenkov
 * @version 1.1
 * @see MainActivity
 */
public class registerActivity extends AppCompatActivity {

    private FirebaseAuth auth;  //The firebase authorization handle.
    private LocalUser usr;  //The user object handle.
    private Location location = new Location(); //Handle on the location object.
    private String logTag = "registerActivity";
    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //The realtime database handle
    private DatabaseReference myRef = database.getReference(); //The reference to the database handle

    /**
     * This function deals with creating the registerActivity when it is called.
     *
     * @author Arseniy Kouzmenkov
     * @param savedInstanceState : This keeps the state of the screen when the app is reopened.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance(); //Connects to the Firebase Authentication service.
    }

    /**
     * This method is executed when the user clicks on the REGISTER button on the screen. It checks
     * that all the fields have been filled in with the minimum necessary information. In addition,
     * it deals with checking if a username is in use already or not.
     *
     * @author Arseniy Kouzmenkov
     * @param view : the handle on the view of this screen.
     */
    public void onRegisterClick(View view){
        //Gets the handle on all the user input fields.
        final EditText username = (EditText) findViewById(R.id.register_username);
        final EditText email = (EditText) findViewById(R.id.register_email);
        final EditText password = (EditText) findViewById(R.id.register_password);
        final EditText country = (EditText) findViewById(R.id.register_country);
        final EditText province = (EditText) findViewById(R.id.register_province);
        final EditText city = (EditText) findViewById(R.id.register_city);

        //Checks that the user has provided any username at all.
        if(username.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a full username.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks that the email field is filled with anything at all.
        if(email.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a valid email.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks that the password is at least 8 characters in length.
        if(password.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Please, provide a password that is more than 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks that the country field was filled with anything at all.
        if(country.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a country.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks that the province field is filled with anything at all.
        if(province.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a province..", Toast.LENGTH_SHORT).show();
            return;
        }

        //Checks that the city field is filled with anything at all.
        if(city.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a city.", Toast.LENGTH_SHORT).show();
            return;
        }

        //Fills in the location object that is required to create a user object later on.
        location.setCountry(country.getText().toString());
        location.setProvinceOrState(province.getText().toString());
        location.setCity(city.getText().toString());

        //Grabs the string representation of the username provided so far.
        String strUsername = username.getText().toString();

        // Check if username alredy exists.
        myRef.child("usernames").child(strUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(logTag, "Got username " + username + " Data from the database");
                if (snapshot.exists()){
                    //Notifies the user that the username is already taken.
                    Log.d(logTag, username + " taken");
                    Toast.makeText(getApplicationContext(), "Username Is taken.", Toast.LENGTH_LONG).show();

                }
                else{
                    Log.d(logTag, username + " not taken");
                    // Create new user.
                    signUp(location, username.getText().toString(), email.getText().toString(), password.getText().toString());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d(logTag, "Database Error", databaseError.toException());
            }
        });


    }

    /**
     * This class attempts to register a user with the provided information. It will close this
     * activity upon success or let the user know that it did not work upon failed registration.
     *
     * @author Tristan Carlson
     * @param location : the general location of the new user.
     * @param userName : the username that the new user wants to use.
     * @param email : the email with which the user is attempting to register
     * @param password : the password with which the user is attempting to register.
     */
    public void signUp(final Location location, final String userName, final String email, final String password){
        // Attempt to create user.
        Log.d(logTag, "In signup");

        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(logTag, "Successfully created user with email: " + email);

                        usr = new LocalUser(location, userName, email, auth.getCurrentUser());

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();

                        auth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(logTag, "User profile updated.");
                                            writeUserToDatabase();
                                            Toast.makeText(getApplicationContext(), "User Successfully registered, please sign in now.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent();
                                            setResult(RESULT_OK, intent);
                                            finish();

                                        }
                                        else{
                                            Log.d(logTag, "User Profile update Failed.  This is bad.");
                                        }
                                    }
                                });


                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // If sign in fails, display a message to the user.
                        Log.w(logTag, "Failed to create user with email: " + email, e);
                        Toast.makeText(getApplicationContext(), "That email has been taken.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void writeUserToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(usr.getUserName()).setValue(usr)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote user to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });
        myRef.child("usernames").child(usr.getUserName()).setValue(usr.getUserid())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Successfully wrote username to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write Username to database", e);
                    }
                });

    }

}
