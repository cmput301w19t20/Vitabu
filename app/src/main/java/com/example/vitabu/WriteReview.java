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


public class WriteReview extends AppCompatActivity {

    private FirebaseDatabase database = FirebaseDatabase.getInstance(); //The realtime database handle
    private DatabaseReference myRef = database.getReference(); //The reference to the database handle

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);


        // get borrowRecord
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BORROWRECORD_MESSAGE);
        Gson gson = new Gson();
        final BorrowRecord record = gson.fromJson(message, BorrowRecord.class);
        final String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        // determine who is the reviewer
        final String ownerName = record.getOwnerName();
        final String borrowerName = record.getBorrowerName();
        final String reviewFrom, reviewTo;
        if (ownerName.equals(userName)){
            reviewFrom = ownerName;
            reviewTo = borrowerName;
        }
        else{
            reviewFrom = borrowerName;
            reviewTo = ownerName;
        }

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
                        Toast.makeText(WriteReview.this, R.string.write_review_success, Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
        );
    }

    public void writeReview(Review review){
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
}
