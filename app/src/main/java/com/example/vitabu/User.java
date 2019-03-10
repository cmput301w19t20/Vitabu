/*
 * This file contains the User model class that has all the information pertaining to the User as
 * it is stored in the Firebase database.
 *
 * Author: Owen Randall
 * Version: 1.5
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import java.util.Date;

/**
 * The User object for Vitabu that encapsulates all the possible attributes of a User
 *
 * @author Owen Randall
 * @see UserAbstract
 * @version 1.5
 */
public class User extends UserAbstract {

    /**
     * This is the default constructor that is needed for Firebase Database to serialize the object
     * into the database.
     */
    public User(){}

    /**
     * This is the constructor that is used to instantiate the User object with all the necessary
     * regular parameters.
     *
     * @param userName the username of the user
     * @param date the date when the user joined
     * @param email the email that is associated with this user.
     */
    public User(String userName, Date date, String email){
        this.userName = userName;
        this.joinDate = date;
        this.email = email;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public Date getJoinDate() {
        return joinDate;
    }

    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserid(){
        return userid;
    }

    public void setUserid(String userid){
        this.userid = userid;
    }
}
