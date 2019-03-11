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
public class BookTest {

    // test book class
    Book book = new Book("Vitabu", "Vitabu Inc", "123456789", "available", "owner name", "This book was good");

    @Test
    public void bookTitle_isCorrect() {
        // tests getters and setters
        assertEquals("Vitabu", book.getTitle());
        book.setTitle("Vitabu 2: The Sequel");
        assertEquals("Vitabu 2: The Sequel", book.getTitle());
    }

    @Test
    public void bookAuthor_isCorrect() {
        // tests getters and setters
        assertEquals("Vitabu Inc", book.getAuthor());
        book.setAuthor("Death of an Author");
        assertEquals("Death of an Author", book.getAuthor());
    }

    @Test
    public void bookISBN_isCorrect() {
        // test getters and setters
        assertEquals("123456789", book.getISBN());
        book.setISBN("987654321");
        assertEquals("987654321", book.getISBN());
    }

    @Test
    public void bookStatus_isCorrect() {
        // test getters and setters
        // test incorrect argument assignment
        assertEquals("available", book.getStatus());
        book.setStatus("requested");
        assertEquals("requested", book.getStatus());
        book.setStatus("accepted");
        assertEquals("accepted", book.getStatus());
        book.setStatus("borrowed");
        assertEquals("borrowed", book.getStatus());
        try {
            book.setStatus("fake status");
        } catch (IllegalArgumentException e) {
            assertEquals(IllegalArgumentException.class, e.getClass());
        }
    }

    @Test
    public void bookDescription_isCorrect(){
        // check book description is correct
        assertEquals("This book was good", book.getDescription());
        book.setDescription("This book wasn't good");
        assertEquals("This book wasn't good", book.getDescription());
    }
}
