package com.example.vitabu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocalUserTest {

    // test LocalUser class
    Location loc = new Location();
    LocalUser user = new LocalUser();

    @Test
    public void localUserLocation_isCorrect() {
        loc.setCountry("Canada");
        user.setLocation(loc);
        assertEquals(loc, user.getLocation());
    }

    @Test
    public void localUserBorrowerRating_isCorrect() {
        user.setBorrowerRating(5);
        assertEquals(5, user.getBorrowerRating());
    }

    @Test
    public void localUserOwnerRating_isCorrect() {
        user.setOwnerRating(8);
        assertEquals(8, user.getOwnerRating());
    }

    @Test
    public void localUserBooksBorrowed_isCorrect() {
        user.setBooksBorrowed(23);
        assertEquals(23, user.getBooksBorrowed());
    }

    @Test
    public void localUserBooksOwner_isCorrect() {
        user.setBooksOwned(103);
        assertEquals(103, user.getBooksOwned());
    }
}
