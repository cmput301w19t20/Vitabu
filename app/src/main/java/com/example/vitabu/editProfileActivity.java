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
 * This file contains the activity that lets the user edit their own profile.
 *
 * Author: Jacob Paton
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.internal.firebase_auth.zzcz;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.FirebaseUserMetadata;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;

import java.util.List;

public class editProfileActivity extends AppCompatActivity {
    User owner;
    User user;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        auth = FirebaseAuth.getInstance();

        // get user object from intent
        Intent intent = getIntent();
        String userMessage = intent.getStringExtra(MainActivity.USER_MESSAGE);
        Gson gson = new Gson();
        user = gson.fromJson(userMessage, User.class);


        // get hold of textviews and populate information
        TextView nameHolder = (TextView) findViewById(R.id.edit_profile_username);
        nameHolder.setText(user.getUserName());
        TextView emailHolder = (TextView) findViewById(R.id.edit_profile_email);
        emailHolder.setText(user.getEmail());
        TextView locationHolder = (TextView) findViewById(R.id.edit_profile_location);
        locationHolder.setText(user.getLocation().getCity());
        TextView booksBorrowedHolder = (TextView) findViewById(R.id.edit_profile_books_borrowed);
        int number = user.getBooksBorrowed();
        String format = getResources().getString(R.string.user_profile_books_borrowed);
        String message = String.format(format, number);
        booksBorrowedHolder.setText(message);
        TextView booksOwnedHolder = (TextView) findViewById(R.id.edit_profile_books_owned);
        number = user.getBooksOwned();
        format = getResources().getString(R.string.user_profile_books_owned);
        message = String.format(format, number);
        booksOwnedHolder.setText(message);
        TextView joinedHolder = (TextView) findViewById(R.id.edit_profile_joined_on);
        joinedHolder.setText(user.getJoinDate().toString());
        RatingBar ratingBar = (RatingBar) findViewById(R.id.edit_profile_borrower_rating_bar);
        ratingBar.setRating(user.getBorrowerRating());
        ratingBar = (RatingBar) findViewById(R.id.edit_profile_owner_rating_bar);
        ratingBar.setRating(user.getOwnerRating());


        // create on click listener for see user reviews button
        Button buttonBorrower = (Button) findViewById(R.id.edit_profile_borrower_review_button);
        buttonBorrower.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(editProfileActivity.this, userReviewActivity.class);
                        Gson gson = new Gson();
                        String message = gson.toJson(user);
                        intent.putExtra(MainActivity.USER_MESSAGE, message);
                        intent.putExtra(MainActivity.REVIEW_TYPE, "borrower");
                        startActivity(intent);
                    }
                }
        );

        Button buttonOwner = (Button) findViewById(R.id.edit_profile_owner_review_button);
        buttonOwner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(editProfileActivity.this, userReviewActivity.class);
                        Gson gson = new Gson();
                        String message = gson.toJson(user);
                        intent.putExtra(MainActivity.USER_MESSAGE, message);
                        intent.putExtra(MainActivity.REVIEW_TYPE, "owner");
                        startActivity(intent);
                    }
                }
        );

        // Create on click listener for save profile button.
        Button saveButton = (Button) findViewById(R.id.edit_profile_save_profile);
        saveButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Save to database and close.
                        String email = ((EditText) findViewById(R.id.edit_profile_email)).getText().toString();
                        String city = ((EditText) findViewById(R.id.edit_profile_location)).getText().toString();

                        auth.getCurrentUser().updateEmail(email).addOnSuccessListener(
                                new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Edit Profile", "Successfully updated user email.");
                                    }
                                }
                        );

                        Location location = new Location();
                        location.setCity(city);

                        user.setLocation(location);

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference();
                        myRef.child("users").child(user.getUserName()).setValue(user)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Edit Profile", "Successfully wrote user to database.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Edit Profile", "Failed to write User to database", e);
                                    }
                                });
                        myRef.child("usernames").child(user.getUserName()).setValue(user.getUserid())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.d("Edit Profile", "Successfully wrote username to database.");
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Log.d("Edit Profile", "Failed to write Username to database", e);
                                    }
                                });

                        finish();
                    }
                });

        // Create onClickListener for cancel button
        Button buttonCancel = (Button) findViewById(R.id.edit_profile_cancel);
        buttonCancel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );
    }
}