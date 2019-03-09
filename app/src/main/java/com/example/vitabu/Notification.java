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
    private String userid;
    private String notificationid;

    public Notification(String title, String message, String type, String userid) {
        this.date = new Date();
        this.title = title;
        this.message = message;
        this.type = type;
        this.seen = false;
        this.userid = userid;
    }

    public Notification(){

    }

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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
