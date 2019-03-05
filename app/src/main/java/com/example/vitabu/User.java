package com.example.vitabu;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String userid;
    private Location location;
    private int borrowerRating;
    private int ownerRating;
    private Date joinDate;
    private String email;
    private ArrayList<Book> ownedBooks;
    private ArrayList<BorrowRecord> borrowedBooks;
    private ArrayList<BorrowRecord> lentBooks;

    public User(String userid, Date joinDate, String email) {
        this.userid = userid;
        this.joinDate = joinDate;
        this.email = email;
    }

    public User(String userid, Location location, int borrowerRating, int ownerRating, Date joinDate, String email) {
        this.userid = userid;
        this.location = location;
        this.borrowerRating = borrowerRating;
        this.ownerRating = ownerRating;
        this.joinDate = joinDate;
        this.email = email;
    }

    public ArrayList<BorrowRecord> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(ArrayList<BorrowRecord> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public void addBorrowedBook(BorrowRecord b){
        borrowedBooks.add(b);
    }

    public void removeBorrowedBook(BorrowRecord b){
        borrowedBooks.remove(b);
    }

    public ArrayList<BorrowRecord> getLentBooks() {
        return lentBooks;
    }

    public void setLentBooks(ArrayList<BorrowRecord> lentBooks) {
        this.lentBooks = lentBooks;
    }

    public void addLentBook(BorrowRecord b){
        lentBooks.add(b);
    }

    public void removeLentBook(BorrowRecord b){
        lentBooks.remove(b);
    }

    public ArrayList<Book> getOwnedBooks(){
        return ownedBooks;
    }

    public void addOwnedBook(Book b){
        ownedBooks.add(b);
    }

    public void removeOwnedBook(Book b){
        ownedBooks.remove(b);
    }

    public void setOwnedBooks(ArrayList<Book> ownedBooks) {
        this.ownedBooks = ownedBooks;
    }

    public String getUserid() {
        return userid;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getBorrowerRating() {
        return borrowerRating;
    }

    public void setBorrowerRating(int borrowerRating) {
        this.borrowerRating = borrowerRating;
    }

    public int getOwnerRating() {
        return ownerRating;
    }

    public void setOwnerRating(int ownerRating) {
        this.ownerRating = ownerRating;
    }

    public Date getJoinDate() {
        return joinDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
