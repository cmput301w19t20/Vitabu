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

    /**
     * This is the constructor that is used to instantiate the Notification object with all the necessary
     * and provided parameters.
     *
     * @param title the title of the book about which the notification is being created.
     * @param message a message that is to be displayed in the notification.
     * @param type a type of the notification.
     * @param userName the username which needs to be notified.
     */
    public Notification(String title, String message, String type, String userName) {
        this.notificationid = UUID.randomUUID().toString();
        this.date = new Date();
        this.title = title;
        this.message = message;
        this.type = type;
        this.seen = false;
        this.userName = userName;
    }

    /**
     * This is the empty default constructor that is necessary for Firebase Database. We also use it
     * when building up the class using the setters alone.
     */
    public Notification(){
        notificationid = UUID.randomUUID().toString();

    }

    /**
     * The getter for the notification's ID
     *
     * @return the notification's ID
     */
    public String getNotificationid() {
        return notificationid;
    }

    /**
     * The setter for the notification's ID
     *
     * @param notificationid the notification's ID
     */
    public void setNotificationid(String notificationid) {
        this.notificationid = notificationid;
    }

    /**
     * The getter for the notification owner's username
     *
     * @return the notification owner's username.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * The setter for the notification owner's username
     *
     * @param userName the notification owner's username
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * The getter for the notification's date
     *
     * @return the notification's date
     */
    public Date getDate() {
        return date;
    }

    /**
     * The setter for the notification's date
     *
     * @param date the notification's date
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * The getter for the title of the book pertaining to this notification
     *
     * @return the title of the book pertaining to this notification.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The setter for the title of the book pertaining to this notification
     *
     * @param title the title of the book pertaining to this notification
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The getter for the message in the notification.
     *
     * @return the message in the notification.
     */
    public String getMessage() {
        return message;
    }

    /**
     * The setter for the message in the notification.
     *
     * @param message the message in the notification
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The getter for the type of notification.
     *
     * @return the type of notification
     */
    public String getType() {
        return type;
    }

    /**
     * The setter for the type of notification
     *
     * @param type the type of notification
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * The getter for the seen flag in the notification
     *
     * @return the value of the seen flag
     */
    public boolean isSeen() {
        return seen;
    }

    /**
     * The setter for the seen flag in the notification
     *
     * @param seen the value of the seen flag
     */
    public void setSeen(boolean seen) {
        this.seen = seen;
    }


}
