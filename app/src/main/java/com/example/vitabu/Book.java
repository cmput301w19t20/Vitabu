package com.example.vitabu;

public class Book {
    private String title;
    private String author;
    private int ISBN;
    private String status;

    public Book(String title, String author, int ISBN, String status) {
        this.title = title;
        this.author = author;
        this.ISBN = ISBN;
        setStatus(status);
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

    public void setStatus(String status) throws IllegalArgumentException{
        if(status != "available" && status != "requested" && status != "accepted" && status != "borrowed") {
            throw new IllegalArgumentException("Status must be one of the following: available, requested, accepted, borrowed.");
        }
        this.status = status;
    }
}