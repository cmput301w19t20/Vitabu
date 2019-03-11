package com.example.vitabu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class NotificationTest {

    // test Notification class
    Notification notif = new Notification("Book Requested", "Your book has been requested", "Request", "VitabuUser");

    @Test
    public void NotificationTitle_isCorrect() {
        assertEquals("Book Requested", notif.getTitle());
    }

    @Test
    public void NotificationMessage_isCorrect() {
        assertEquals("Your book has been requested", notif.getMessage());
    }

    @Test
    public void NotificationType_isCorrect() {
        assertEquals("Request", notif.getType());
    }

    @Test
    public void NotificationUsername_isCorrect() {
        assertEquals("VitabuUser", notif.getUserName());
    }

    @Test
    public void NotificationSeen_isCorrect() {
        assertEquals(false, notif.isSeen());
        notif.setSeen(true);
        assertEquals(true, notif.isSeen());
    }
}
