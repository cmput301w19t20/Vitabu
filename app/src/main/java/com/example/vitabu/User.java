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

    /**
     * The getter for the location of the User.
     *
     * @return the location of the user.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * The setter for the location of the user.
     *
     * @param location the location of the user.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * The getter for the user's username.
     *
     * @return the user's username
     */
    public String getUserName() {
        return userName;
    }

    /**
     * The setter for the user's username
     *
     * @param userName the user's username.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * The getter for the borrower rating of the user.
     *
     * @return the borrower rating of the user.
     */
    public int getBorrowerRating() {
        return borrowerRating;
    }

    /**
     * The setter for the borrower rating of the user.
     *
     * @param borrowerRating the borrower rating of the user.
     */
    public void setBorrowerRating(int borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    /**
     * The getter of the owner rating of the user.
     *
     * @return the owner rating of the user.
     */
    public int getOwnerRating() {
        return ownerRating;
    }

    /**
     * The setter for the owner rating of the user.
     *
     * @param ownerRating the owner rating of the user.
     */
    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }

    /**
     * The getter of the number of books owned by the user.
     *
     * @return the number of books owned by the user.
     */
    public int getBooksOwned() {
        return booksOwned;
    }

    /**
     * The setter for the number of books owned by the user.
     *
     * @param booksOwned the number of books owned by the user.
     */
    public void setBooksOwned(int booksOwned) {
        this.booksOwned = booksOwned;
    }

    /**
     * The getter for the number of books that the user borrowed.
     *
     * @return the number of books that the user has borrowed.
     */
    public int getBooksBorrowed() {
        return booksBorrowed;
    }

    /**
     * The setter of the number of books that the user has borrowed
     *
     * @param booksBorrowed the number of books that the user has borrowed.
     */
    public void setBooksBorrowed(int booksBorrowed) {
        this.booksBorrowed = booksBorrowed;
    }

    /**
     * The getter for the date when the user joined the platform.
     *
     * @return the date when the user joined the platform.
     */
    public Date getJoinDate() {
        return joinDate;
    }

    /**
     * The setter for the date when the user joined the platform.
     *
     * @param joinDate the date when the user joined the platform.
     */
    public void setJoinDate(Date joinDate) {
        this.joinDate = joinDate;
    }

    /**
     * The getter for the user's email
     *
     * @return the user's email
     */
    public String getEmail() {
        return email;
    }

    /**
     * The setter for the user's email
     *
     * @param email the user's email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * The getter for the user's UserID.
     *
     * @return the user's UserID
     */
    public String getUserid(){
        return userid;
    }

    /**
     * The setter for the user's UserID
     *
     * @param userid the user's UserID
     */
    public void setUserid(String userid){
        this.userid = userid;
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
}
