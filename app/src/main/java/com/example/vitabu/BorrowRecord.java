package com.example.vitabu;

import java.util.Date;

/**
 * @author davidowe
 * @version 1.0
 * Object used to keep track of borrow/lending transactions between users. Both lender and borrower should receive a copy of this record.
 * Doubles as a book request, if approved is false.
 */
public class BorrowRecord {
    private User owner;
    private User borrower;
    private Book book;
    private Date dateBorrowed;
    private Location pickUpLocation;
    private boolean approved;

    public BorrowRecord(User owner, User borrower, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.book = book;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public User getBorrower() {
        return borrower;
    }

    public void setBorrower(User borrower) {
        this.borrower = borrower;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
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
