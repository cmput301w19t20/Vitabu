package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Singleton Class to encapsulate all logic for interacting with the database.
 *
 * @author Tristan Carlson
 * @version 1.0
 */
public class Database {
    private String logTag = "Database Singleton";
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private DatabaseReference rootReference;
    private static final Database ourInstance = new Database();

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        rootReference = database.getReference();
    }

    public void createUser(final Runnable callbackMethod, final String email, final String password, final String userName, final Location location){
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        Log.d(logTag, "Successfully created user with email: " + email);

                        LocalUser user = new LocalUser(location, userName, email, auth.getCurrentUser());

                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();

                        auth.getCurrentUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(logTag, "User profile updated.");
                                            //this.writeUserToDatabase();
                                            //Toast.makeText(getApplicationContext(), "User Successfully registered, please sign in now.", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent();
                                            //setResult(RESULT_OK, intent);
                                            //finish();
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
                        //Toast.makeText(getApplicationContext(), "That email has been taken.",
                         //       Toast.LENGTH_SHORT).show();
                    }
                });
    }


}

