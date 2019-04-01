/*
Vitabu is an Open Source application available under the Apache (Version 2.0) License.

Copyright 2019 Arseniy Kouzmenkov, Owen Randall, Ayooluwa Oladosu, Tristan Carlson, Jacob Paton,
Katherine Richards

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

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
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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
    private Database databaseSingleton = Database.getInstance();

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
        final EditText city = (EditText) findViewById(R.id.register_city);
        final EditText reenter_password = (EditText) findViewById(R.id.register_reenter_password);

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
            Toast.makeText(getApplicationContext(), "Please, provide a password that is at least 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }


        if(!reenter_password.getText().toString().equals(password.getText().toString())){
            Toast.makeText(getApplicationContext(), "Please, type in the same password in both the Password and Re-enter Password fields.", Toast.LENGTH_SHORT).show();
            return;

        }

        //Checks that the city field is filled with anything at all.
        if(city.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a city.", Toast.LENGTH_SHORT).show();
            return;
        }

        location.setCity(city.getText().toString());

        //Grabs the string representation of the username provided so far.
        final String strUsername = username.getText().toString();

        // Check if username alredy exists.
        Runnable fail = new Runnable() {
            @Override
            public void run() {
                usernameTaken(strUsername);
            }
        };

        Runnable success = new Runnable() {
            @Override
            public void run() {
                signUp(location, username.getText().toString(), email.getText().toString(), password.getText().toString());
            }
        };

        databaseSingleton.checkUsernameAvailability(success, fail, strUsername);
    }

    public void usernameTaken(String username){
        Log.d(logTag, username + " taken");
        Toast.makeText(getApplicationContext(), "Username Is taken.", Toast.LENGTH_LONG).show();
    }

    /**
     * This method attempts to register a user with the provided information. It will close this
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
        Runnable success = new Runnable() {
                @Override
                public void run() {
                    signUpSuccess();
                }
        };

        Runnable fail = new Runnable() {
            @Override
            public void run() {
                signUpFail();
            }
        };
        databaseSingleton.createUser(success, fail, email, password, userName, location);
    }


    public void signUpFail(){
        Log.d(logTag, "in Callback fail");
        Log.w(logTag, "Failed to create user with email: ");
        Toast.makeText(getApplicationContext(), "That email has been taken.",
                Toast.LENGTH_SHORT).show();
    }

    public void signUpSuccess(){
        Log.d(logTag, "in Callback success");
        Toast.makeText(getApplicationContext(), "User Successfully registered, please sign in now.", Toast.LENGTH_LONG).show();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
        finish();
    }

}
