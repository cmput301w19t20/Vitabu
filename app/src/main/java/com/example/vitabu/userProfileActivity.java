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
 * This file contains the UserProfile Activity which displays a User's profile to the screen.
 *
 * Author: Ayooluwa Oladosu
 * Version: 1.3
 * Outstanding Issues: ---
 */
package com.example.vitabu;
/*
References:

Shane Conder & Lauren Darcey. "Android SDK Quick Tip: Formatting Resource Strings." Envato, https://code.tutsplus.com/tutorials/android-sdk-quick-tip-formatting-resource-strings--mobile-1775
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.gson.Gson;

public class userProfileActivity extends AppCompatActivity {
    User user;

    //This method is called on start up of this activity and will populate the necessary fields with
    //information that people will be able to see when viewing a user profile.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        // get user object from intent
        Intent intent = getIntent();
        String userMessage = intent.getStringExtra(MainActivity.USER_MESSAGE);
        Gson gson = new Gson();
        user = gson.fromJson(userMessage, User.class);

        // get hold of textviews and populate information
        TextView nameHolder = (TextView) findViewById(R.id.user_profile_username);
        nameHolder.setText(user.getUserName());
        TextView emailHolder = (TextView) findViewById(R.id.user_profile_email);
        emailHolder.setText(user.getEmail());
        TextView locationHolder = (TextView) findViewById(R.id.user_profile_location);
        locationHolder.setText(user.getLocation().getCity());
        TextView booksBorrowedHolder = (TextView) findViewById(R.id.user_profile_books_borrowed);
        int number = user.getBooksBorrowed();
        String format = getResources().getString(R.string.user_profile_books_borrowed);
        String message = String.format(format, number);
        booksBorrowedHolder.setText(message);
        TextView booksOwnedHolder = (TextView) findViewById(R.id.user_profile_books_owned);
        number = user.getBooksOwned();
        format = getResources().getString(R.string.user_profile_books_owned);
        message = String.format(format, number);
        booksOwnedHolder.setText(message);
        TextView joinedHolder = (TextView) findViewById(R.id.user_profile_joined_on);
        joinedHolder.setText(user.getJoinDate().toString());
        RatingBar ratingBar = (RatingBar) findViewById(R.id.user_profile_borrower_rating_bar);
        ratingBar.setRating(user.getBorrowerRating());
        ratingBar = (RatingBar) findViewById(R.id.user_profile_owner_rating_bar);
        ratingBar.setRating(user.getOwnerRating());


        // create on click listener for see user reviews button
        Button buttonBorrower = (Button) findViewById(R.id.user_profile_borrower_review_button);
        buttonBorrower.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(userProfileActivity.this, userReviewActivity.class);
                        Gson gson = new Gson();
                        String message = gson.toJson(user);
                        intent.putExtra(MainActivity.USER_MESSAGE, message);
                        intent.putExtra(MainActivity.REVIEW_TYPE, "borrower");
                        startActivity(intent);
                    }
                }
        );

        Button buttonOwner = (Button) findViewById(R.id.user_profile_owner_review_button);
        buttonOwner.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(userProfileActivity.this, userReviewActivity.class);
                        Gson gson = new Gson();
                        String message = gson.toJson(user);
                        intent.putExtra(MainActivity.USER_MESSAGE, message);
                        intent.putExtra(MainActivity.REVIEW_TYPE, "owner");
                        startActivity(intent);
                    }
                }
        );
    }
}