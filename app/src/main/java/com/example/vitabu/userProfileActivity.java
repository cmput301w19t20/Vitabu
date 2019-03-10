package com.example.vitabu;
/*
References:

Shane Conder & Lauren Darcey. "Android SDK Quick Tip: Formatting Resource Strings." Envato, https://code.tutsplus.com/tutorials/android-sdk-quick-tip-formatting-resource-strings--mobile-1775
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

/**
 * Activity is created following 1 of 2 possible cases:
 * Case 1: after "Owner Profile" is selected from bookInfoActivity (UI C)
 * Case 2: user should be able to view their own profile, access point currently undecided
 *
 * TODO: add "see all reviews" in layout .xml file
 * TODO: display in full or in part user's owned and borrowed books in this activity
 * TODO: fetch all relevant information to display in this activity ..
 */

public class userProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // get user object from intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Gson gson = new Gson();
        final User user = gson.fromJson(message, User.class);

        //final LocalUser user = MainActivity.t;

        // setting temporary default rating for borrower and owner bars
        // also set in .xml file for each rating bar for viewing purposes
        // so actually I'm not sure why I've put this here ...
//        borrowerRating.setRating(3.5f);
//        ownerRatingBar.setRating(5.0f);

        // get hold of textviews and populate information
        TextView nameHolder = (TextView) findViewById(R.id.user_profile_username);
        nameHolder.setText(user.getUserName());
        TextView emailHolder = (TextView) findViewById(R.id.user_profile_email);
        emailHolder.setText(user.getEmail());
        //TextView locationHolder = (TextView) findViewById(R.id.user_profile_location);
        TextView booksBorrowedHolder = (TextView) findViewById(R.id.user_profile_books_borrowed);
        int number = user.getBooksBorrowed();
        String format = getResources().getString(R.string.user_profile_books_borrowed);
        message = String.format(format, number);
        booksBorrowedHolder.setText(message);
        TextView booksOwnedHolder = (TextView) findViewById(R.id.user_profile_books_owned);
        number = user.getBooksOwned();
        format = getResources().getString(R.string.user_profile_books_borrowed);
        message = String.format(format, number);
        booksOwnedHolder.setText(message);
        TextView joinedHolder = (TextView) findViewById(R.id.user_profile_joined_on);
        //joinedHolder.setText(user.getJoinDate().toString());
        joinedHolder.setText("join date");
        RatingBar ratingBar = (RatingBar) findViewById(R.id.user_profile_borrower_rating_bar);
        ratingBar.setRating(user.getBorrowerRating());
        ratingBar = (RatingBar) findViewById(R.id.user_profile_owner_rating_bar);
        ratingBar.setRating(user.getOwnerRating());


        // create on click listener for see user reviews button
        Button button = (Button) findViewById(R.id.user_profile_review_button);
        button.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(userProfileActivity.this, userReviewActivity.class);
                        Gson gson = new Gson();
                        String message = gson.toJson(user);
                        intent.putExtra(MainActivity.EXTRA_MESSAGE, message);
                        startActivity(intent);
                    }
                }
        );
    }
}