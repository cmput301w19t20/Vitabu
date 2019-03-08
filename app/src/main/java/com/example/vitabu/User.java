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


import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * @version 1.0
 * An object encapsulating all the attributes and information pertaining to a user.
 */
public class User {
    private Location location;
    private int borrowerRating;
    private int ownerRating;
    private ArrayList<Book> ownedBooks;
    private ArrayList<BorrowRecord> borrowedBooks;
    private ArrayList<BorrowRecord> lentBooks;
    private ArrayList<Notification> notifications;
    private FirebaseUser firebaseUser;
    private String logTag = "User object";



    /**
     * Constructor for use when reconstructing already created user.
     */
    public User(FirebaseUser user){
        // Attempt to sign in a user with email.
        firebaseUser = user;
        // TODO Pull All other data from database.
    }

    /**
     * Constructor for use when creating a new user.
     */
    public User(Location location, FirebaseUser user) {
        this.location = location;
        this.borrowerRating = 0;
        this.ownerRating = 0;
        firebaseUser = user;
    }

    /**
     * Writes This class to the database.
     */
    public void writeToDatabase(){
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        myRef.child("users").child(this.getUserName()).setValue(this)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Suscsesfully wrote user to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write User to database", e);
                    }
                });
        myRef.child("usernames").child(this.getUserName()).setValue(this.getUserid())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(logTag, "Suscsesfully wrote username to database.");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(logTag, "Failed to write Username to database", e);
                    }
                });

    }

    public String getUserName() {
        return firebaseUser.getDisplayName();
    }


    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {
        this.notifications = notifications;
    }

    public void addNotiication(Notification notification){
        notifications.add(notification);
    }

    public void removeNotification(Notification notification){
        notifications.remove(notification);
    }

    public ArrayList<BorrowRecord> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<BorrowRecord> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void addBorrowedBook(BorrowRecord b){
        borrowedBooks.add(b);
    }

    public void removeBorrowedBook(BorrowRecord b){
        borrowedBooks.remove(b);
    }

    public ArrayList<BorrowRecord> getLentBooks() {
        return lentBooks;
    }

    public void setLentBooks(ArrayList<BorrowRecord> lentBooks) {
        this.lentBooks = lentBooks;
    }

    public void addLentBook(BorrowRecord b){
        lentBooks.add(b);
    }

    public void removeLentBook(BorrowRecord b){
        lentBooks.remove(b);
    }

    public ArrayList<Book> getOwnedBooks(){
        return ownedBooks;
    }

    public void addOwnedBook(Book b){
        ownedBooks.add(b);
    }

    public void removeOwnedBook(Book b){
        ownedBooks.remove(b);
    }

    public void setOwnedBooks(ArrayList<Book> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public String getUserid() {
        return firebaseUser.getUid();
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
        // TODO Deal with possible nullptr exception.
        return new Date(firebaseUser.getMetadata().getCreationTimestamp());
    }

    public String getEmail() {
        return firebaseUser.getEmail();
    }

    public void setEmail(String email) {
        firebaseUser.updateEmail(email);
    }


    public FirebaseUser getFireBaseUser(){
        return firebaseUser;
    }

}
