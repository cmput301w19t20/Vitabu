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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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
        // TODO Pull All other data from database.
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

    public void setUserid(String userid){
        this.userid = userid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getBorrowerRating() {
        return borrowerRating;
    }

    public void setBorrowerRating(int borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
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
        // TODO Deal with possible nullptr exception.
        return new Date(firebaseUser.getMetadata().getCreationTimestamp());
    }

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

//    public void setEmail(String email) {
//        // TODO Uncomment this.
//        //firebaseUser.updateEmail(email);
//        //this.email = email;
//    }

    public int getBooksOwned() {
        return booksOwned;
    }

    public void setBooksOwned(int booksOwned) {
        this.booksOwned = booksOwned;
    }

    public int getBooksBorrowed() {
        return booksBorrowed;
    }

    public void setBooksBorrowed(int booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
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
