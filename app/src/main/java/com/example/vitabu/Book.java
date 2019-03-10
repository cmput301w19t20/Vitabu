/*
 * This file contains the model class for books. This file is needed to store book information into
 * the database.
 *
 * Author: Owen Randall
 * Version: 1.3
 * Outstanding Issues: No place to store the ID of the images stored in Firestore.
 */

package com.example.vitabu;


import android.util.Log;

import java.util.UUID;

/**
 * The Book object for Vitabu that encapsulates all the possible attributes of a book
 *
 * @version 1.3
 * @author Owen Randall
 */
public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String status;
    private String ownerName;
    private String description;
    private String bookid;

    /**
     * This is the constructor that is used to instantiate the Book object with all the necessary
     * regular parameters.
     *
     * @param title the title of the book.
     * @param author the author of the book
     * @param ISBN the ISBN of the book
     * @param status the status of the book (borrowed, available, requested, accepted).
     * @param ownerName Username of the owner of the book.
     */
    public Book(String title, String author, String ISBN, String status, String ownerName, String description) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
        this.ownerName = ownerName;
        this.bookid = UUID.randomUUID().toString();
        this.description = description;
    }

    /**
     * This is the empty default constructor that is necessary for Firebase Database. We also use it
     * when building up the class using the setters alone.
     */
    public Book(){
        bookid = UUID.randomUUID().toString();
    }

    /**
     * @return returns the bookId. A string.
     */
    public String getBookid() {
        return bookid;
    }

    /**
     * @param bookid the bookId. A string.
     */
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * @return returns the title of the book. A string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title of the book. A string.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return returns the author of the book. A string.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author the author of the book. A string.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return returns the ISBN of the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * @param ISBN the isbn of the book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * @return returns the status of the book
     */
    public String getStatus() {
        return status;
    }


    /**
     * Can only set the status to one of the following: requested, accepted, borrowed, or available.
     * Any other value throws an error.
     * @param status the value to which status needs to be set to.
     * @throws IllegalArgumentException
     */
    public void setStatus(String status) throws IllegalArgumentException{
        if(! status.equals("available") && ! status.equals("requested") && ! status.equals("accepted") && ! status.equals("borrowed") ) {
            throw new IllegalArgumentException("Status must be one of the following: available, requested, accepted, borrowed.");
        }
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
