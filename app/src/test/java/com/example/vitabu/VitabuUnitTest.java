package com.example.vitabu;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class VitabuUnitTest {


    // test book class
    Book book = new Book();
    book.setTitle("Vitabu");
    book.setAuthor("Vitabu Inc");
    book.setISBN(123456789);
    book.setStatus("available");

    @Test
    public void bookTitle_isCorrect() {
        assertEquals("Vitabu", book.getTitle());
    }
    @Test
    public void bookAuthor_isCorrect() {
        assertEquals("Vitabu Inc", book.getAuthor());
    }
    @Test
    public void bookISBN_isCorrect() {
        assertEquals(123456789, book.getISBN());
    }
    @Test
    public void bookStatus_isCorrect() {
        assertEquals("available", book.getStatus());
    }


    // test user class
    User user = new Book();
    user.setUserid("Vitabu User");
    user.setLocation("Edmonton");
    user.setBorrowRating(4);
    user.setOwnerRating(3);
    Date date = new Date();
    user.setJoinDate(date);
    user.setEmail("Vitabu@gmail.com");

    @Test
    public void userUserid_isCorrect() {
        assertEquals("Vitabu User", user.getUserid());
    }
    @Test
    public void userLocation_isCorrect() {
        assertEquals("Edmonton", user.getLocation());
    }
    @Test
    public void userBorrowRating_isCorrect() {
        assertEquals(4, user.getBorrowRating());
    }
    @Test
    public void userOwnerRating_isCorrect() {
        assertEquals(3, user.getOwnerRating());
    }
    @Test
    public void userJoinDate_isCorrect() {
        assertEquals(date, user.getJoinDate());
    }
    @Test
    public void userEmail_isCorrect() {
        assertEquals("Vitabu@gmail.com", user.getEmail());
    }
}