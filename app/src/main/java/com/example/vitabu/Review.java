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
 * This file contains the model class for the Review object. It is used to create and read reviews
 * to and from the Firebase Database.
 *
 * Author: Owen Randall
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import java.util.Date;
import java.util.UUID;

/**
 * A Review object to keep track of the information relevant to a user review.
 *
 * @author Owen Randall
 * @version 1.0
 */
public class Review {
    private String reviewid;
    private String ownerName;
    private String borrowerName;
    private String reviewFrom;
    private String reviewTo;
    private Date date;
    private int rating;
    private String body;

    /**
     * This is the default constructor that is used by Firebase Database and whenever we want to
     * create a Review object without pre-populating it with specific data.
     */
    public Review(){

    }

    /**
     * This the constructor that instantiates the Review object with the specific provided parameters.
     *
     * @param ownerName the username of the Owner of the book
     * @param borrowerName the username of the borrower of the book
     * @param rating the rating of the review being given.
     * @param body the body of the review.
     * @param reviewFrom the reviewer's username
     * @param reviewTo the username of the recipient of the review
     */
    public Review(String ownerName, String borrowerName, int rating, String body, String reviewFrom, String reviewTo) {
        this.ownerName = ownerName;
        this.borrowerName = borrowerName;
        this.rating = rating;
        this.body = body;
        this.date = new Date();
        this.reviewid = UUID.randomUUID().toString();
        this.reviewFrom = reviewFrom;
        this.reviewTo = reviewTo;
    }

    /**
     * The getter for the review ID
     *
     * @return the review ID
     */
    public String getReviewid() {
        return reviewid;
    }

    /**
     * The setter for the review ID
     *
     * @param reviewid the review ID
     */
    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    /**
     * The getter for the username of the Owner of the book
     *
     * @return the username of the Owner of the book
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * The setter for the username of the Owner of the book
     *
     * @param ownerName the username of the Owner of the book
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * The getter for the username of the borrower of the book
     *
     * @return the username of the borrower of the book
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * The setter for the username of the borrower of the book
     *
     * @param borrowerName the username of the borrower of the book
     */
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    /**
     * The getter for the date when the review was created
     *
     * @return the date when the review was created
     */
    public Date getDate() {
        return date;
    }

    /**
     * The setter for the date when the review was created
     *
     * @param date the date when the review was created
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * The getter for the rating of the review being given.
     *
     * @returnthe rating of the review being given.
     */
    public int getRating() {
        return rating;
    }

    /**
     * This is a setter for the rating that only permits the rating to be within a specific range of
     * values.
     *
     * @param rating the rating that we want to set.
     * @throws IllegalArgumentException an exception when someone tries to set the rating with data
     *                                  that is outside of the range of accepted values.
     */
    public void setRating(int rating) throws IllegalArgumentException {
        if(rating > 5 || rating < 0){
            throw new IllegalArgumentException();
        }
        this.rating = rating;
    }

    /**
     * The getter for the body of the review.
     *
     * @return the body of the review.
     */
    public String getBody() {
        return body;
    }

    /**
     * The setter for the body of the review.
     *
     * @param body the body of the review.
     */
    public void setBody(String body) {
        this.body = body;
    }

    /**
     * The getter for the reviewer's username
     *
     * @return the reviewer's username
     */
    public String getReviewFrom() {
        return reviewFrom;
    }

    /**
     * The setter for the reviewer's username
     *
     * @param reviewFrom the reviewer's username
     */
    public void setReviewFrom(String reviewFrom) {
        this.reviewFrom = reviewFrom;
    }

    /**
     * The getter for the username of the recipient of the review
     *
     * @return the username of the recipient of the review
     */
    public String getReviewTo() {
        return reviewTo;
    }

    /**
     * The setter for the username of the recipient of the review
     *
     * @param reviewTo the username of the recipient of the review
     */
    public void setReviewTo(String reviewTo) {
        this.reviewTo = reviewTo;
    }
}

