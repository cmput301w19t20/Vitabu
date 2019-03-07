package com.example.vitabu;


/**
 * @author davidowe
 * @version 1.0
 * The Book object for Vitabu that encapsulates all the possible attributes of a book
 */
public class Book {
    private String title;
    private String author;
    private int ISBN;
    private String status;
    private User owner;

    public Book(String title, String author, int ISBN, String status, User owner) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
        this.owner = owner;
    }

    public Book(){

    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public int getISBN() {
        return ISBN;
    }

    public void setISBN(int ISBN) {
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
}
