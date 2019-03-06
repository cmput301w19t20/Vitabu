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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class registerActivity extends AppCompatActivity {

    private FirebaseAuth auth;  //The firebase authorization handle.
    private User usr;  //The user object handle.
    private Location location; //Handle on the location object.
    private String logTag = "registerActivity";
    private boolean signUpAttemptReturnValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the firebase authorization service
        auth = FirebaseAuth.getInstance();
    }

    public void updateReturnValue(boolean value){
        signUpAttemptReturnValue = value;
    }

    public boolean attemptSignUp(final String email, String password){

        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(logTag, "Successfully created user with email: " + email);
                            //FirebaseUser user = auth.getCurrentUser();
                            //updateUI(user);
                            updateReturnValue(true);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(logTag, "Failed to create user with email: " + email, task.getException());
                            Toast.makeText(getApplicationContext(), "Sign Up failed. The user with this email may already exist.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                            updateReturnValue(false);
                        }
                    }
                });
        return signUpAttemptReturnValue;
    }

    public void onRegisterClick(View view){
        String temp;

        EditText username = (EditText) findViewById(R.id.register_username);
        EditText email = (EditText) findViewById(R.id.register_email);
        EditText password = (EditText) findViewById(R.id.register_password);
        EditText country = (EditText) findViewById(R.id.register_country);
        EditText province = (EditText) findViewById(R.id.register_province);
        EditText city = (EditText) findViewById(R.id.register_city);


        if(username.getText().toString().length() < 1){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a full username.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(email.getText().toString().length() < 1){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a valid email.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(password.getText().toString().length() < 8){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a password that is more than 8 characters long.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(country.getText().toString().length() < 1){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a country.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(province.getText().toString().length() < 1){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a province..", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if(city.getText().toString().length() < 1){
            Toast toast = Toast.makeText(getApplicationContext(), "Please, provide a city.", Toast.LENGTH_SHORT);
            toast.show();
            return;
        }

        if (attemptSignUp(email.getText().toString(), password.getText().toString())){
            return;
        }
        else {
            Intent intent = new Intent();
            Toast.makeText(getApplicationContext(), "User Successfully registered, please sign in now.", Toast.LENGTH_LONG).show();

            setResult(RESULT_OK, intent);
            finish();
        }

    }
}
