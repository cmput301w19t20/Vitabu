package com.example.vitabu;

import java.util.ArrayList;
import java.util.Date;

public class User {
    private String userid;
    private String location;
    private int borrowerRating;
    private int ownerRating;
    private Date joinDate;
    private String email;
    ArrayList<Book> ownedBooks;
    ArrayList<Book> borrowedBooks;

    public User(String userid, Date joinDate, String email) {
        this.userid = userid;
        this.joinDate = joinDate;
        this.email = email;
    }

    public User(String userid, String location, int borrowerRating, int ownerRating, Date joinDate, String email) {
        this.userid = userid;
        this.location = location;
        this.borrowerRating = borrowerRating;
        this.ownerRating = ownerRating;
        this.joinDate = joinDate;
        this.email = email;
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

    public ArrayList<Book> getBorrowedBooks(){
        return borrowedBooks;
    }

    public void addBorrowedBook(Book b){
        borrowedBooks.add(b);
    }

    public void removeBorrowedBook(Book b){
        borrowedBooks.remove(b);
    }

    public String getUserid() {
        return userid;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
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
