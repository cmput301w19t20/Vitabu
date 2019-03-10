package com.example.vitabu;


import java.util.UUID;

/**
 * @version 1.0
 * The Book object for Vitabu that encapsulates all the possible attributes of a book
 */
public class Book {
    private String title;
    private String author;
    private String ISBN;
    private String status;
    private String ownerName;
    private String description;
    private String bookid;

    public Book(String title, String author, String ISBN, String status, String ownerName) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
        this.ownerName = ownerName;
    }

    public Book(){
        bookid = UUID.randomUUID().toString();
    }

    public String getBookid() {
        return bookid;
    }

    public void setBookid(String bookid) {
        this.bookid = bookid;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBN() {
        return ISBN;
    }

    public void setISBN(String ISBN) {
        this.ISBN = ISBN;
    }

    public String getStatus() {
        return status;
    }


    /**
     * Can only set the status to one of the following: requested, accepted, borrowed, or available. Any other value throws an error.
     * @param status
     * @throws IllegalArgumentException
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
