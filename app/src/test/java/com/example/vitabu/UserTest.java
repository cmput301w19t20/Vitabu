package com.example.vitabu;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class UserTest {

    // test User class
    Location loc = new Location();
    Date date = new Date();
    User user3 = new User("Vitabu User", date, "Vitabu@gmail.com");

    @Test
    public void userUserid_isCorrect() {
        // test constructor
        assertEquals("Vitabu User", user3.getUserName());
    }

    @Test
    public void userJoinDate_isCorrect() {
        // test constructorss
        assertEquals(date, user3.getJoinDate());
    }

    @Test
    public void userEmail_isCorrect() {
        // test getters and setters
        assertEquals("Vitabu@gmail.com", user3.getEmail());
        user3.setEmail("newemail.com");
        assertEquals("newemail.com", user3.getEmail());
    }

    @Test
    public void userLocation_isCorrect() {
        // test getters and setters
        user3.setLocation(loc);
        assertEquals(loc, user3.getLocation());
    }

    @Test
    public void userBorrowRating_isCorrect() {
        // test getters and setters
        user3.setBorrowerRating(4);
        assertEquals(4, user3.getBorrowerRating());
    }

    @Test
    public void userOwnerRating_isCorrect() {
        // test getters and setters
        user3.setOwnerRating(3);
        assertEquals(3, user3.getOwnerRating());
    }

    @Test
    public void userName_isCorrect() {
        // test constructors
        assertEquals("Vitabu User", user3.getUserName());
    }

    @Test
    public void userBookOwnedNum_isCorrect() {
        // test constructors
        user3.setBooksOwned(6);
        assertEquals(6, user3.getBooksOwned());
    }

    @Test
    public void userBookBorrowedNum_isCorrect() {
        // test constructors
        user3.setBooksBorrowed(4);
        assertEquals(4, user3.getBooksBorrowed());
    }

}
