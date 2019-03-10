package com.example.vitabu;

import java.util.Date;

public abstract class UserAbstract extends Object{
    protected String userid;
    protected Location location;
    protected String userName;
    protected int borrowerRating;
    protected int ownerRating;
    protected int booksOwned;
    protected int booksBorrowed;
    protected Date joinDate;
    protected String email;

    public abstract Location getLocation();
    public abstract void setLocation(Location l);
    public abstract String getUserName();
    public abstract void setUserName(String s);
    public abstract int getBorrowerRating();
    public abstract void setBorrowerRating(int i);
    public abstract int getOwnerRating();
    public abstract void setOwnerRating(int i);
    public abstract int getBooksOwned();
    public abstract void setBooksOwned(int i);
    public abstract int getBooksBorrowed();
    public abstract void setBooksBorrowed(int i);
    public abstract Date getJoinDate();
    public abstract void setJoinDate(Date d);
    public abstract String getEmail();
    //public abstract void setEmail(String s);
    public abstract String getUserid();
    public abstract void setUserid(String s);
}
