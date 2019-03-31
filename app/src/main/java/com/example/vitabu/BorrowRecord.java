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
 * This file contains the BorrowRecord model class that is used to keep track of the current status
 * of a borrowing.
 *
 * Author: Owen Randall
 * Version: 1.3
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;
import java.util.UUID;

/**
 * Object used to keep track of borrow/lending transactions between users. Both lender and borrower should receive a copy of this record.
 * Doubles as a book request, if approved is false.
 *
 * @version 1.3
 * @author Owen Randall
 */
public class BorrowRecord {
    private String ownerName;
    private String borrowerName;
    private String bookid;
    private Date dateBorrowed;
    private Location pickUpLocation;
    private boolean approved;
    private String recordid;
    private String ownerPhoneNumber;
    private String ownerNotes;
    private String ownerEmail;

    
    /**
     * This is the constructor that is used to instantiate the BorrowRecord object with all the necessary
     * and specified parameters.
     *
     * @param ownerName the name of the owner of the book.
     * @param borrowerName the name of the borrower of the book.
     * @param bookid the id of the book that is being borrowed.
     */
    public BorrowRecord(String ownerName, String borrowerName, String bookid) {
        this.ownerName = ownerName;
        this.borrowerName = borrowerName;
        this.bookid = bookid;
        this.dateBorrowed = new Date();
        this.recordid = UUID.randomUUID().toString();
        this.approved = false;
    }

    /**
     * This is the default constructor that is used to instantiate the BorrowRecord without any parameters.
     * This is used with the Firebase Database for serialization.
     */
    public BorrowRecord(){
        this.recordid = UUID.randomUUID().toString();
        this.approved = false;

    }

    /**
     * The getter for the Owner's phone number
     *
     * @return the owner's phone number
     */
    public String getOwnerPhoneNumber(){return ownerPhoneNumber;}

    /**
     * The getter for the Owner's notes
     *
     * @return the owner's notes
     */
    public String getOwnerNotes(){return ownerNotes;}

    /**
     * The getter for the owner's email.
     *
     * @return the owner's email
     */
    public String getOwnerEmail(){return ownerEmail;}

    /**
     * The setter for the Owner's phone number
     *
     * @param number the owner's phone number
     */
    public void setOwnerPhoneNumber(String number){ownerPhoneNumber = number;}

    /**
     * The setter for the Owner's notes
     *
     * @param notes the Owner's notes
     */
    public void setOwnerNotes(String notes){ownerNotes = notes;}

    /**
     * The setter for the Owner's email
     *
     * @param email the owner's email.
     */
    public void setOwnerEmail(String email){ownerEmail = email;}

    /**
     * The getter for the ID of the record.
     *
     * @return the ID of the record.
     */
    public String getRecordid() {
        return recordid;
    }

    /**
     * The setter for the ID of the record.
     *
     * @param recordid the ID of the record.
     */
    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    /**
     * The getter for the approved flag.
     *
     * @return the approved flag.
     */
    public boolean isApproved() {
        return approved;
    }

    /**
     * The setter for the approved flag.
     *
     * @param approved the value of the approved flag.
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * The getter for the Owner's username.
     *
     * @return the owner's username
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * The setter for the owner's username
     *
     * @param userName the owner's username
     */
    public void setOwnerName(String userName) {
        this.ownerName = userName;
    }

    /**
     * The getter for the borrower's username
     *
     * @return the borrower's username
     */
    public String getBorrowerName() {
        return borrowerName;
    }

    /**
     * The setter for the borrower's username
     *
     * @param borrowerName the borrower's username
     */
    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    /**
     * The getter for the Book's ID
     *
     * @return the Book's ID
     */
    public String getBookid() {
        return bookid;
    }

    /**
     * The setter for the Book's ID
     *
     * @param bookid the Book's ID
     */
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    /**
     * The getter for the Date for the book being initially borrowed
     *
     * @return the Date for the book being initially being borrowed.
     */
    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    /**
     * The setter for the date for the book being initially borrowed
     *
     * @param dateBorrowed the date for the book being initially borrowed
     */
    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    /**
     * The getter for the pickup location.
     *
     * @return the pickup location
     */
    public Location getPickUpLocation() {
        return pickUpLocation;
    }

    /**
     * The setter for the pickup location.
     *
     * @param pickUpLocation the pickup location.
     */
    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }

}
