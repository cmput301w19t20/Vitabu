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

    public String getReviewid() {
        return reviewid;
    }

    public void setReviewid(String reviewid) {
        this.reviewid = reviewid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getReviewFrom() {
        return reviewFrom;
    }

    public void setReviewFrom(String reviewFrom) {
        this.reviewFrom = reviewFrom;
    }

    public String getReviewTo() {
        return reviewTo;
    }

    public void setReviewTo(String reviewTo) {
        this.reviewTo = reviewTo;
    }
}

