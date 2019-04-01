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
 * This file contains the class of the LocalUser that lets us keep track of which user is currently
 * signed in and all the data pertaining to it.
 *
 * Author: Owen Randall
 * Version: 1.2
 * Outstanding Issues: Deal with potential null pointer exceptions in getJoinDate().
 */
package com.example.vitabu;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * An object encapsulating all the attributes and information pertaining to the current user using the app.
 *
 * @version 1.2
 * @author Owen Randall
 */
public class LocalUser extends UserAbstract {
    private String logTag = "User object";


    /**
     * Constructor for use when reconstructing already created user.
     *
     * @deprecated
     */
    public LocalUser(FirebaseUser user){
        super();
    }

    /**
     * The default constructor that may be used by the Firebase Database.
     */
    public LocalUser(){
        super();
    }

    /**
     * The constructor that may be used when creating the LocalUser with specific parameters.
     *
     * @param location the general location of the user.
     * @param userName the username of the user
     * @param email the email with which the user signed up
     * @param user an instance of a Firebase Authentication user.
     */
    public LocalUser(Location location, String userName, String email, FirebaseUser user) {
        this.location = location;
        this.borrowerRating = 0;
        this.ownerRating = 0;
        this.booksBorrowed = 0;
        this.booksBorrowed = 0;
        this.userName = userName;
        this.email = email;
        this.userid = user.getUid();
    }


    /**
     * This is a getter for the username from the Firebase Authentication server.
     *
     * @return the username of the current Firebase user.
     */
    public String getUserName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser().getDisplayName();
    }

    /**
     * This is the setter for the user's username.
     * @param userName the user's username.
     */
    public void setUserName(String userName){
        this.userName = userName;
    }

    /**
     * This is a getter for the UserID from the Firebase Authentication server.
     *
     * @return the UserID of the current Firebase user.
     */
    public String getUserid() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser().getUid();
    }

    /**
     * The setter for the user's userid.
     *
     * @param userid the user's userid
     */
    public void setUserid(String userid){
        this.userid = userid;
    }

    /**
     * The getter for the User's general location.
     *
     * @return the User's general location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The setter for the user's general location.
     *
     * @param location the user's general location.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * The getter for the user's borrower rating.
     *
     * @return the user's borrower rating.
     */
    public float getBorrowerRating() {
        return borrowerRating;
    }

    /**
     * The setter for the user's borrower rating.
     *
     * @param borrowerRating the user's borrower rating.
     */
    public void setBorrowerRating(float borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    /**
     * The getter for the user's owner rating.
     *
     * @return the user's owner rating.
     */
    public float getOwnerRating() {
        return ownerRating;
    }

    /**
     * The setter for the user's owner rating.
     *
     * @param ownerRating the user's owner rating.
     */
    public void setOwnerRating(float ownerRating) {
        this.ownerRating = ownerRating;
    }

    /**
     * This is a getter for the date when the user created their account.
     *
     * @return the date when the user registered.
     */
    public Date getJoinDate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return new Date(firebaseUser.getMetadata().getCreationTimestamp());
    }

    /**
     * The setter for the date when the user joined
     *
     * @param joinDate the date when the user joined
     */
    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }

    /**
     * This is a getter for the email that the user registered with. We get the email from the Firebase
     * Authentication user.
     *
     * @return the email with which the user registered.
     */
    public String getEmail() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        return firebaseUser.getEmail();
    }

    /**
     * The getter for the number of books the user owns.
     *
     * @return the number of books the user owns.
     */
    public int getBooksOwned() {
        return booksOwned;
    }

    /**
     * The setter for the number of books that the user owns.
     *
     * @param booksOwned the number of books that the user owns.
     */
    public void setBooksOwned(int booksOwned) {
        this.booksOwned = booksOwned;
    }

    /**
     * The getter for the number of books that the user borrowed.
     *
     * @return the number of books that the user borrowed.
     */
    public int getBooksBorrowed() {
        return booksBorrowed;
    }

    /**
     * The setter for the number of books that the user borrowed.
     *
     * @param booksBorrowed the number of books that the user borrowed.
     */
    public void setBooksBorrowed(int booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    /**
     * The getter of the number of owner reviews of the user.
     *
     * @return the number of owner reviews
     */
    public int getNumOwnerReviews() {
        return numOwnerReviews;
    }

    /**
     * The setter for the number of owner reviews of the user.
     *
     * @param numReviews the number of owner reviews
     */
    public void setNumOwnerReviews(int numReviews) {
        this.numOwnerReviews = numReviews;
    }

    /**
     * The getter of the number of owner reviews of the user.
     *
     * @return the number of borrower reviews
     */
    public int getNumBorrowerReviews() {
        return numBorrowerReviews;
    }

    /**
     * The setter for the number of owner reviews of the user.
     *
     * @param numReviews the number of borrower reviews
     */
    public void setNumBorrowerReviews(int numReviews) {
        this.numBorrowerReviews = numReviews;
    }

    /**
     * This converts this class to JSON and returns it.
     *
     * @return the JSON string representing this class.
     */
    public String toJson(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
