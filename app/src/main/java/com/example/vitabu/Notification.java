package com.example.vitabu;

import java.util.Date;

/**
 * @author davidowe
 * @version 1.0
 * A Notification object specific to a user that should show up in their notifications
 */
public class Notification {
    private Date date;
    private String title;
    private String message;
    private String type;
    private boolean seen;
    private User user;

    public Notification(){

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

}
