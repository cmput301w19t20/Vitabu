package com.example.vitabu;

import java.util.Date;

public class BorrowRecord {
    private User owner;
    private User borrower;
    private Book book;
    private Date dateBorrowed;
    private Location pickUpLocation;

    public BorrowRecord(User owner, User borrower, Book book) {
        this.owner = owner;
        this.borrower = borrower;
        this.book = book;
        dateBorrowed = new Date();
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
