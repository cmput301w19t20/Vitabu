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

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;


import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * @version 1.0
 * An object encapsulating all the attributes and information pertaining to a user.
 */
public class LocalUser extends UserAbstract {
    private String logTag = "User object";


    /**
     * @deprecated
     * Constructor for use when reconstructing already created user.
     */
    public LocalUser(FirebaseUser user){
        super();
        // TODO Pull All other data from database.
    }

    public LocalUser(){
        super();

    }

    /**
     * Constructor for use when creating a new user.
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


    public String getUserName() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        return auth.getCurrentUser().getDisplayName();

        //return firebaseUser.getDisplayName();
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

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

    public Date getJoinDate() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        // TODO Deal with possible nullptr exception.
        return new Date(firebaseUser.getMetadata().getCreationTimestamp());
    }

    public void setJoinDate(Date joinDate){
        this.joinDate = joinDate;
    }

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

}
