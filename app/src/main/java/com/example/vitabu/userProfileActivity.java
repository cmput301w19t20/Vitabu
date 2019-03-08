package com.example.vitabu;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RatingBar;

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

        // setting temporary default rating for borrower and owner bars
        // also set in .xml file for each rating bar for viewing purposes
        // so actually I'm not sure why I've put this here ...
//        borrowerRating.setRating(3.5f);
//        ownerRatingBar.setRating(5.0f);


    }
}
