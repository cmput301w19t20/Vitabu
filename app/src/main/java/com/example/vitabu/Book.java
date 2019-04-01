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
    private String borrower;

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
    public Book(String title, String author, String ISBN, String status, String ownerName, String description, String borrower) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
        this.ownerName = ownerName;
        this.bookid = UUID.randomUUID().toString();
        this.description = description;
        this.borrower = borrower;
    }

    /**
     * This is the empty default constructor that is necessary for Firebase Database. We also use it
     * when building up the class using the setters alone.
     */
    public Book(){
        bookid = UUID.randomUUID().toString();
    }

    /**
     * The getter for the BookID
     *
     * @return returns the bookId. A string.
     */
    public String getBookid() {
        return bookid;
    }

    /**
     * The setter for the BookID.
     *
     * @deprecated as the book ID is set in the constructor through the UUID library.
     * @param bookid the bookId. A string.
     */
    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    /**
     * The getter for the owner's username
     *
     * @return the owner's username
     */
    public String getOwnerName() {
        return ownerName;
    }

    /**
     * The setter for the owner's username of the book.
     *
     * @param ownerName the username of the owner of the book.
     */
    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    /**
     * The getter for the title of the book.
     *
     * @return returns the title of the book. A string.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The setter for the title of the book.
     *
     * @param title the title of the book. A string.
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The getter for the author of the book.
     *
     * @return returns the author of the book. A string.
     */
    public String getAuthor() {
        return author;
    }

    /**
     * The setter for the author of the book.
     *
     * @param author the author of the book. A string.
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * The getter for the ISBN of the book.
     *
     * @return returns the ISBN of the book
     */
    public String getISBN() {
        return ISBN;
    }

    /**
     * The setter for the ISBN of the book.
     *
     * @param ISBN the isbn of the book
     */
    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    /**
     * This is a getter for the status of the book
     *
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

    /**
     * The getter for the description of the book.
     *
     * @return the description of the book.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the description of the book.
     *
     * @param description the description of the book
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Sets the borrower.
     *
     * @param borrower The string username of the user borrowing the book.
     */
    public void setBorrower(String borrower) {
        this.borrower = borrower;
    }

    /**
     * Getter for borrower.
     *
     * @return Returns the username of the user currently borrowing (can be empty).
     */
    public String getBorrower() {
        return borrower;
    }
}
