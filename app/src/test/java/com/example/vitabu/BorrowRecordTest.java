package com.example.vitabu;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class BorrowRecordTest {

    // test Borrow Record
    Book book = new Book("Vitabu", "Vitabu Inc", "123456789", "available", "owner name", "This book was good", "Vitabu Borrower");
    Date date = new Date();
    User user = new User("Vitabu User", date, "Vitabu@gmail.com");
    User user2 = new User("Vitabu User2", date, "Vitabu2@gmail.com");
    BorrowRecord rec = new BorrowRecord(user.getUserName(), user2.getUserName(), book.getBookid());
    Location loc = new Location();

    @Test
    public void recOwner_isCorrect() {
        // test constructor
        assertEquals(user.getUserName(), rec.getOwnerName());
    }

    @Test
    public void recBorrower_isCorrect() {
        // test getters and setters
        assertEquals(user2.getUserName(), rec.getBorrowerName());
        rec.setBorrowerName(user.getUserName());
        assertEquals(user.getUserName(), rec.getBorrowerName());
    }

    @Test
    public void recLocation_isCorrect() {
        // test getters and setters
        rec.setPickUpLocation(loc);
        assertEquals(loc, rec.getPickUpLocation());
    }

    @Test
    public void recBook_isCorrect() {
        // test getters and setters
        rec.setBookid(book.getBookid());
        assertEquals(book.getBookid(), rec.getBookid());
    }

    @Test
    public void recDate_isCorrect() {
        // test getters and setters
        rec.setDateBorrowed(date);
        assertEquals(date, rec.getDateBorrowed());
    }

    @Test
    public void recApproved_isCorrect() {
        // test getters and setters
        assertEquals(false, rec.isApproved());
        rec.setApproved(true);
        assertEquals(true, rec.isApproved());
    }

    @Test
    public void recOwnerPhoneNumber_isCorrect() {
        // test getters and setters
        rec.setOwnerPhoneNumber("123456789");
        assertEquals("123456789", rec.getOwnerPhoneNumber());
    }

    @Test
    public void recOwnerNotes_isCorrect() {
        // test getters and setters
        rec.setOwnerNotes("Owner notes: please be careful with book");
        assertEquals("Owner notes: please be careful with book", rec.getOwnerNotes());
    }

    @Test
    public void recOwnerEmail_isCorrect() {
        // test getters and setters
        rec.setOwnerEmail("Owner@email.com");
        assertEquals("Owner@email.com", rec.getOwnerEmail());
    }
}
