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
 * This file contains the function which will write the reviews for the different users.
 *
 * Author: Ayooluwa Oladosu
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;


public class WriteReviewActivity extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //The realtime database handle
    private DatabaseReference myRef = database.getReference(); //The reference to the database handle
    User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        // get borrowRecord
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.NOTIFICATION_MESSAGE);
        Gson gson = new Gson();
        final Notification notif = gson.fromJson(message, Notification.class);
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        // determine who is the reviewer
        message = notif.getMessage();
        String[] messageArray =  message.split(" ");
        String owner, borrower;
        if (messageArray[1].equals("returned")){
            owner = userName;
            borrower = messageArray[0];
        }
        else{
            owner = messageArray[0];
            borrower = userName;
        }

        final String ownerName = owner;
        final String borrowerName = borrower;
        final String reviewFrom = userName;
        final String reviewTo = messageArray[0];

        // populate textViews
        String format = getResources().getString(R.string.write_review_of_name);
        message = String.format(format, reviewTo);
        final TextView reviewed = (TextView) findViewById(R.id.write_review_of_name);
        reviewed.setText(message);

        // create on click listener for submit review button
        Button button = (Button) findViewById(R.id.write_review_submit_button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get fields for making a review object
                        RatingBar ratingBar = (RatingBar) findViewById(R.id.write_review_ratingBar);
                        int rating = Math.round(ratingBar.getRating());
                        EditText review_message = (EditText) findViewById(R.id.write_review_body);
                        String body = review_message.getText().toString();
                        Review review = new Review(ownerName, borrowerName, rating, body, reviewFrom, reviewTo);
                        writeReview(review);
                        updateRating(review);
                        Toast.makeText(WriteReviewActivity.this, R.string.write_review_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }

    private void writeReview(Review review){
        myRef.child("reviews").child(review.getReviewid()).setValue(review)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("Write Review activity", "Successfully wrote book to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Write Review activity", "Failed to write User to database", e);
                    }
                });
    }

    private void updateRating(Review  review) {
        // update rating of person reviewed
        final Review review2 = review;
        myRef.child("users").orderByChild("userName").equalTo(review.getReviewTo()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                            user = postSnapshot.getValue(User.class);
                        }
                        if (user != null) {
                            modifyRating(review2, user);
                        }
                        else{
                            Log.e("user status", "null as null can be");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }
        );
    }

    private void modifyRating(Review review, User user){
        // update the user's rating
        String username = user.getUserName();
        if (username.equals(review.getOwnerName())){
            float rating = user.getOwnerRating();
            int numReviews = user.getNumOwnerReviews();
            rating = (rating*numReviews + review.getRating())/(numReviews+1);
            user.setOwnerRating(rating);
            user.setNumOwnerReviews(numReviews +1);
        }
        else{
            float rating = user.getBorrowerRating();
            int numReviews = user.getNumBorrowerReviews();
            rating = (rating*numReviews + review.getRating())/(numReviews+1);
            user.setBorrowerRating(rating);
            user.setNumBorrowerReviews(numReviews +1);
        }

        // write back to database
        myRef.child("users").child(user.getUserName()).setValue(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("user review", "Successfully updated user's rating in database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("user review", "Failed to update user rating in database.");
                    }
                }
                );

    }
}
