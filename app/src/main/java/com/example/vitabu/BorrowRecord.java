package com.example.vitabu;

import java.util.Date;
import java.util.UUID;

/**
 * @version 1.0
 * Object used to keep track of borrow/lending transactions between users. Both lender and borrower should receive a copy of this record.
 * Doubles as a book request, if approved is false.
 */
public class BorrowRecord {
    private String userName;
    private String borrowerName;
    private String bookid;
    private Date dateBorrowed;
    private Location pickUpLocation;
    private boolean approved;
    private String recordid;
    private String ownerPhoneNumber;
    private String ownerNotes;
    private String ownerEmail;


    public BorrowRecord(String userName, String borrowerName, String bookid) {
        this.userName = userName;
        this.borrowerName = borrowerName;
        this.bookid = bookid;
        dateBorrowed = new Date();
        recordid = UUID.randomUUID().toString();
    }

    public BorrowRecord(){
        recordid = UUID.randomUUID().toString();

    }

    public String getOwnerPhoneNumber(){return ownerPhoneNumber;}

    public String getOwnerNotes(){return ownerNotes;}

    public String getOwnerEmail(){return ownerEmail;}

    public void setOwnerPhoneNumber(String number){ownerPhoneNumber = number;}

    public void setOwnerNotes(String notes){ownerNotes = notes;}

    public void setOwnerEmail(String email){ownerEmail = email;}

    public String getRecordid() {
        return recordid;
    }

    public void setRecordid(String recordid) {
        this.recordid = recordid;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getBorrowerName() {
        return borrowerName;
    }

    public void setBorrowerName(String borrowerName) {
        this.borrowerName = borrowerName;
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public Date getDateBorrowed() {
        return dateBorrowed;
    }

    public void setDateBorrowed(Date dateBorrowed) {
        this.dateBorrowed = dateBorrowed;
    }

    public Location getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(Location pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
