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
public class VitabuUnitTest {


    // test book class
    Book book = new Book("Vitabu", "Vitabu Inc", 123456789, "available");

    @Test
    public void bookTitle_isCorrect() {
        assertEquals("Vitabu", book.getTitle());
        book.setTitle("Vitabu 2: The Sequel");
        assertEquals("Vitabu 2: The Sequel", book.getTitle());
    }
    @Test
    public void bookAuthor_isCorrect() {
        assertEquals("Vitabu Inc", book.getAuthor());
        book.setAuthor("Death of an Author");
        assertEquals("Death of an Author", book.getAuthor());
    }
    @Test
    public void bookISBN_isCorrect() {
        assertEquals(123456789, book.getISBN());
        book.setISBN(987654321);
        assertEquals(987654321, book.getISBN());
    }
    @Test
    public void bookStatus_isCorrect() {
        assertEquals("available", book.getStatus());
        book.setStatus("requested");
        assertEquals("requested", book.getStatus());
        book.setStatus("accepted");
        assertEquals("accepted", book.getStatus());
        book.setStatus("borrowed");
        assertEquals("borrowed", book.getStatus());
        try{
            book.setStatus("fake status");
        }
        catch (IllegalArgumentException e){
            assertEquals(new IllegalArgumentException().getClass(), e.getClass());
        }
    }

    // test Location class
    Location loc = new Location();

    @Test
    public void locCountry_isCorrect(){
        loc.setCountry("Canada");
        assertEquals("Canada", loc.getCountry());
    }

    @Test
    public void locProvinceOrState_isCorrect(){
        loc.setProvinceOrState("Alberta");
        assertEquals("Alberta", loc.getProvinceOrState());
    }

    @Test
    public void locCity_isCorrect(){
        loc.setCity("Edmonton");
        assertEquals("Edmonton", loc.getCity());
    }

    @Test
    public void locAddress_isCorrect(){
        loc.setAddress("UofA");
        assertEquals("UofA", loc.getAddress());
    }
    Date date = new Date();
    User user = new User("Vitabu User", date, "Vitabu@gmail.com");
    User user2 = new User("Vitabu User2", date, "Vitabu2@gmail.com");
    // test Borrow Record
    BorrowRecord rec = new BorrowRecord(user, user2, book);

    @Test
    public void recOwner_isCorrect(){
        assertEquals(user, rec.getOwner());
    }

    @Test
    public void recBorrower_isCorrect(){
        assertEquals(user2, rec.getBorrower());
        rec.setBorrower(user);
        assertEquals(user, rec.getBorrower());
    }

    @Test
    public void recLocation_isCorrect(){
        rec.setPickUpLocation(loc);
        assertEquals(loc, rec.getPickUpLocation());
    }

    @Test
    public void recBook_isCorrect(){
        rec.setBook(book);
        assertEquals(book, rec.getBook());
    }
    @Test
    public void recDate_isCorrect(){
        rec.setDateBorrowed(date);
        assertEquals(date, rec.getDateBorrowed());
    }



    // test user class
    User user3 = new User("Vitabu User", date, "Vitabu@gmail.com");
    ArrayList<BorrowRecord> borrowed = new ArrayList<BorrowRecord>();
    ArrayList<BorrowRecord> lent = new ArrayList<BorrowRecord>();
    ArrayList<Book> owned = new ArrayList<Book>();


    @Test
    public void userUserid_isCorrect() {
        assertEquals("Vitabu User", user3.getUserid());
    }
    @Test
    public void userJoinDate_isCorrect() {
        assertEquals(date, user3.getJoinDate());
    }
    @Test
    public void userEmail_isCorrect() {
        assertEquals("Vitabu@gmail.com", user3.getEmail());
        user3.setEmail("newemail.com");
        assertEquals("newemail.com", user3.getEmail());
    }
    @Test
    public void userLocation_isCorrect() {
        user3.setLocation(loc);
        assertEquals(loc, user3.getLocation());
    }
    @Test
    public void userBorrowRating_isCorrect() {
        user3.setBorrowerRating(4);
        assertEquals(4, user3.getBorrowerRating());
    }
    @Test
    public void userOwnerRating_isCorrect() {
        user3.setOwnerRating(3);
        assertEquals(3, user3.getOwnerRating());
    }
    @Test
    public void userBorrowedBooks_isCorrect() {
        user3.setBorrowedBooks(new ArrayList<BorrowRecord>());
        assertEquals(borrowed, user3.getBorrowedBooks());
        user3.addBorrowedBook(rec);
        borrowed.add(rec);
        assertEquals(borrowed, user3.getBorrowedBooks());
        user3.removeBorrowedBook(rec);
        borrowed.remove(rec);
        assertEquals(borrowed, user3.getBorrowedBooks());
    }
    @Test
    public void userLentBooks_isCorrect() {
        user3.setLentBooks(new ArrayList<BorrowRecord>());
        assertEquals(lent, user3.getLentBooks());
        user3.addLentBook(rec);
        lent.add(rec);
        assertEquals(lent, user3.getLentBooks());
        user3.removeLentBook(rec);
        lent.remove(rec);
        assertEquals(lent, user3.getLentBooks());
    }
    @Test
    public void userOwnedBooks_isCorrect() {
        user3.setOwnedBooks(new ArrayList<Book>());
        assertEquals(owned, user3.getOwnedBooks());
        user3.addOwnedBook(book);
        owned.add(book);
        assertEquals(owned, user3.getOwnedBooks());
        user3.removeOwnedBook(book);
        owned.remove(book);
        assertEquals(owned, user3.getOwnedBooks());
    }
}