package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;


public class WriteReview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_review);

        /*
        // get borrowRecord
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.BORROWRECORD_MESSAGE);
        Gson gson = new Gson();
        final BorrowRecord record = gson.fromJson(message, BorrowRecord.class);
        String userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        */

        // populate textViews
        String format = getResources().getString(R.string.write_review_of_name);
        //message = String.format(format, userName);
        String message = String.format(format, "Temp");
        TextView reviewed = (TextView) findViewById(R.id.write_review_of_name);
        reviewed.setText(message);



        // create on click listener for submit review button
        Button button = (Button) findViewById(R.id.user_profile_review_button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // get fields for making a review object
                        //String ownerName = record.getOwnerName();
                        //String borrowerName = record.getBorrowerName();
                        RatingBar ratingBar = (RatingBar) findViewById(R.id.write_review_ratingBar);
                        int rating = Math.round(ratingBar.getRating());
                        EditText review_message = (EditText) findViewById(R.id.write_review_body);
                        String body = review_message.getText().toString();
                        Review review = new Review(ownerName, borrowerName, rating, body);
                        finish();
                    }
                }
        );

    }
}
