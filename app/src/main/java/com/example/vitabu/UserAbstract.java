/*
Vitabu is an Open Source application available under the Apache (Version 2.0) License.

Copyright 2019 Arseniy Kouzmenkov, Owen Randall, Ayooluwa Oladosu, Tristan Carlson, Jacob Paton,
Katherine Richards

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
associated documentation files (the "Software"), to deal in the Software without restriction,
including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense,
and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial
portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT
LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

/*
 * This file contains the UserAbstract object that the User and LocalUser classes extend.
 *
 * Author: Owen Randall
 * Version: 1.0
 * Outstanding Issues: ---
 */
package com.example.vitabu;

import java.util.Date;

public abstract class UserAbstract extends Object{
    protected String userid;
    protected Location location;
    protected String userName;
    protected float borrowerRating;
    protected float ownerRating;
    protected int booksOwned;
    protected int booksBorrowed;
    protected Date joinDate;
    protected String email;
    protected int numOwnerReviews;
    protected int numBorrowerReviews;

    public abstract Location getLocation();
    public abstract void setLocation(Location l);
    public abstract String getUserName();
    public abstract void setUserName(String s);
    public abstract float getBorrowerRating();
    public abstract void setBorrowerRating(float i);
    public abstract float getOwnerRating();
    public abstract void setOwnerRating(float i);
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
    public abstract int getNumOwnerReviews();
    public abstract void setNumOwnerReviews(int i);
    public abstract int getNumBorrowerReviews();
    public abstract void setNumBorrowerReviews(int i);
}
