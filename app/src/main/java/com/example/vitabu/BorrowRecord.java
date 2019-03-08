package com.example.vitabu;

import java.util.Date;

/**
 * @author davidowe
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

    public BorrowRecord(String ownerid, String borrowerid, String bookid) {
        this.ownerid = ownerid;
        this.borrowerid = borrowerid;
        this.bookid = bookid;
        dateBorrowed = new Date();
    }

    public BorrowRecord(){

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

    public void setOwner(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getBorrowerid() {
        return borrowerid;
    }

    public void setBorrower(String borrowerid) {
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
