package com.example.vitabu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {

    private FirebaseAuth auth;  //The firebase authorization handle.
    private User usr;  //The user object handle.
    private Location location; //Handle on the location object.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Initialize the firebase authorization service
        auth = FirebaseAuth.getInstance();
    }



    public void onRegisterClick(View view){

    }
}
