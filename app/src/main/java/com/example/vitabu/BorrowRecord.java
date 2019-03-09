package com.example.vitabu;

import java.util.Date;

/**
 * @version 1.0
 * Object used to keep track of borrow/lending transactions between users. Both lender and borrower should receive a copy of this record.
 * Doubles as a book request, if approved is false.
 */
public class BorrowRecord {
    private String ownerid;
    private String borrowerid;
    private String bookid;
    private Date dateBorrowed;
    private Location pickUpLocation;
    private boolean approved;
    private String recordid;
    private String ownerPhoneNumber;
    private String ownerNotes;
    private String ownerEmail;

    public BorrowRecord(String ownerid, String borrowerid, String bookid) {
        this.ownerid = ownerid;
        this.borrowerid = borrowerid;
        this.bookid = bookid;
        dateBorrowed = new Date();
    }

    public BorrowRecord(){

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

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getBorrowerid() {
        return borrowerid;
    }

    public void setBorrowerid(String borrowerid) {
        this.borrowerid = borrowerid;
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
