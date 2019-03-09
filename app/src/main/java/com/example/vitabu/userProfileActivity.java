package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    User owner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        final RatingBar borrowerRating = (RatingBar) findViewById(R.id.user_profile_borrower_rating_bar);
        final RatingBar ownerRatingBar = (RatingBar) findViewById(R.id.user_profile_owner_rating_bar);

        // get user object from intent
        Intent intent = getIntent();
        String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        Gson gson = new Gson();
        IntentJson passed = gson.fromJson(message, IntentJson.class);
        LocalUser user = passed.getUser();

        // setting temporary default rating for borrower and owner bars
        // also set in .xml file for each rating bar for viewing purposes
        // so actually I'm not sure why I've put this here ...
//        borrowerRating.setRating(3.5f);
//        ownerRatingBar.setRating(5.0f);

        // get hold of textviews and populate information
        TextView nameHolder = (TextView) findViewById(R.id.user_profile_username);
        nameHolder.setText(user.getUserid());
        TextView emailHolder = (TextView) findViewById(R.id.user_profile_email);
        nameHolder.setText(user.getEmail());
        TextView locationHolder = (TextView) findViewById(R.id.user_profile_location);
        locationHolder.setText(user.getLocation().getCity());
        TextView booksBorrowedHolder = (TextView) findViewById(R.id.user_profile_books_borrowed);
        int size = user.getBooksBorrowed();
        booksBorrowedHolder.setText("Number of Books Borrowed: " + Integer.toString(size));
        size = user.getBooksOwned();
        TextView booksOwnedHolder = (TextView) findViewById(R.id.user_profile_books_owned);
        booksOwnedHolder.setText("Number of Books Owned: " + Integer.toString(size));
        TextView joinedHolder = (TextView) findViewById(R.id.user_profile_joined_on);
        joinedHolder.setText(user.getJoinDate().toString());
        RatingBar ratingBar = (RatingBar) findViewById(R.id.user_profile_borrower_rating_bar);
        ratingBar.setRating(user.getBorrowerRating());
        ratingBar = (RatingBar) findViewById(R.id.user_profile_owner_rating_bar);
        ratingBar.setRating(user.getOwnerRating());
    }
}