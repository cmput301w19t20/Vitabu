/*
 * This file contains the model class for the Location object. It is used to set the meeting location
 * and also keep track of a User's general location.
 *
 * Author: Owen Randall
 * Version: 1.0
 * Outstanding Issues: Potential issue with integration with Google Maps fragment.
 */

package com.example.vitabu;

/**
 * A Location object to keep track of country, province, city, and (optionally) address.
 *
 * @author Owen Randall
 * @version 1.0
 * @see User
 */
public class Location {
    private String country;
    private String provinceOrState;
    private String city;
    private String address;

    /**
     * This is the default constructor used by Firebase Database and whenever we want to create a
     * Location object without pre-populating it with anything.
     */
    public Location(){

    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvinceOrState() {
        return provinceOrState;
    }

    public void setProvinceOrState(String provinceOrState) {
        this.provinceOrState = provinceOrState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}