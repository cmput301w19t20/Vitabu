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

public class registerActivity extends AppCompatActivity {

    private FirebaseAuth auth;  //The firebase authorization handle.
    private User usr;  //The user object handle.
    private Location location = new Location(); //Handle on the location object.
    private String logTag = "registerActivity";
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference myRef = database.getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        auth = FirebaseAuth.getInstance();
    }


    public void onRegisterClick(View view){

        final EditText username = (EditText) findViewById(R.id.register_username);
        final EditText email = (EditText) findViewById(R.id.register_email);
        final EditText password = (EditText) findViewById(R.id.register_password);
        final EditText country = (EditText) findViewById(R.id.register_country);
        final EditText province = (EditText) findViewById(R.id.register_province);
        final EditText city = (EditText) findViewById(R.id.register_city);

        if(username.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a full username.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(email.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a valid email.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(password.getText().toString().length() < 8){
            Toast.makeText(getApplicationContext(), "Please, provide a password that is more than 8 characters long.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(country.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a country.", Toast.LENGTH_SHORT).show();
            return;
        }

        if(province.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a province..", Toast.LENGTH_SHORT).show();
            return;
        }

        if(city.getText().toString().length() < 1){
            Toast.makeText(getApplicationContext(), "Please, provide a city.", Toast.LENGTH_SHORT).show();
            return;
        }


        location.setCountry(country.getText().toString());
        location.setProvinceOrState(province.getText().toString());
        location.setCity(city.getText().toString());
        String strUsername = username.getText().toString();
        // Check if username alredy exists.
        myRef.child("usernames").child(strUsername).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Log.d(logTag, "got data");
                if (snapshot.exists()){
                    Log.d(logTag, "uname taken");
                    Toast.makeText(getApplicationContext(), "Username Is taken.", Toast.LENGTH_LONG).show();

                }
                else{
                    Log.d(logTag, "uname not taken");
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



    public void signUp(final Location location, final String userName, final String email, final String password){
        // Attempt to create user.
        Log.d(logTag, "In signup");
        auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(logTag, "Successfully created user with email: " + email);
                        usr = new User(location, auth.getCurrentUser());

                        // TODO Deal with failed user account update.
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(userName)
                                .build();

                        usr.getFireBaseUser().updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d(logTag, "User profile updated.");
                                            usr.writeToDatabase();
                                        }
                                    }
                                });

                        Toast.makeText(getApplicationContext(), "User Successfully registered, please sign in now.", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent();
                        setResult(RESULT_OK, intent);
                        finish();
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


}
