/*
 * This file contains the model class for the Notification object. It is used to create Notifications
 * and put them into the Firebase database. It is used to display a notification about an event to
 * the user.
 *
 * Author: Owen Randall
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import java.util.Date;
import java.util.UUID;

/**
 * A Notification object specific to a user that should show up in their notifications
 *
 * @author Owen Randall
 * @version 1.0
 */
public class Notification {
    private Date date;
    private String title;
    private String message;
    private String type;
    private boolean seen;
    private String userName;
    private String notificationid;
    private String borrowRecordId;

    /**
     * This is the constructor that is used to instantiate the Notification object with all the necessary
     * and provided parameters.
     *
     * @param title the title of the book about which the notification is being created.
     * @param message a message that is to be displayed in the notification.
     * @param type a type of the notification.
     * @param userName the username which needs to be notified.
     */
    public Notification(String title, String message, String type, String userName, String borrowRecordId) {

        this.notificationid = UUID.randomUUID().toString();
        this.date = new Date();
        this.title = title;
        this.message = message;
        this.type = type;
        this.seen = false;
        this.userName = userName;
        this.borrowRecordId = borrowRecordId;
    }

    /**
     * This is the empty default constructor that is necessary for Firebase Database. We also use it
     * when building up the class using the setters alone.
     */
    public Notification(){
        notificationid = UUID.randomUUID().toString();

    }

    public String getNotificationid() {
        return notificationid;
    }

    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    public String getBorrowRecordId(){
        return borrowRecordId;
    }

    public void setBorrowRecordId(String borrowRecordId){
        this.borrowRecordId = borrowRecordId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        if(! type.equals("request") && ! type.equals("accept") && ! type.equals("dropoff") && ! type.equals("return")  && ! type.equals("pickup") && ! type.equals("review")) {
            throw new IllegalArgumentException("Type must be one of the following: request, accept, dropoff, return, pickup, review");
        }
        this.type = type;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }


}
