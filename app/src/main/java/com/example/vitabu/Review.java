package com.example.vitabu;

import java.util.Date;
import java.util.UUID;

public class Review {
    private String reviewid;
    private String ownerName;
    private String borrowerName;
    private Date date;
    private int rating;
    private String body;

    public Review(){

    }

    public Review(String ownerName, String borrowerName, int rating, String body) {
        this.ownerName = ownerName;
        this.borrowerName = borrowerName;
        this.rating = rating;
        this.body = body;
        this.date = new Date();
        this.reviewid = UUID.randomUUID().toString();
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
}
