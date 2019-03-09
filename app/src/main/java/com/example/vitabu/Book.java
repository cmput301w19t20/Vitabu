package com.example.vitabu;


/**
 * @version 1.0
 * The Book object for Vitabu that encapsulates all the possible attributes of a book
 */
public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String status;
    private String ownerid;
    private String description;
    private String bookid;


    public Book(String title, String author, String ISBN, String status, String ownerid) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
        this.ownerid = ownerid;
    }

    public Book(){

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
    
    /**
     * @return returns the ownerid. A string.
     */
    public String getOwnerid() {
        return ownerid;
    }

    /**
     * @param ownerid the ownerId of the book. A string.
     */
    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
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
     * Can only set the status to one of the following: requested, accepted, borrowed, or available. Any other value throws an error.
     * @param status a string representing the status of the book. One of requested, accepted, borrowed, or available.
     * @throws IllegalArgumentException if status is not one of requested, accepted, borrowed, or available, an error is thrown
     */
    public void setStatus(String status) throws IllegalArgumentException{
        if(status != "available" && status != "requested" && status != "accepted" && status != "borrowed") {
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
