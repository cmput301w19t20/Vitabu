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

    /**
     * The getter for the country.
     *
     * @return the country
     */
    public String getCountry() {
        return country;
    }

    /**
     * The setter for the country.
     *
     * @param country the country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * The getter for the province
     *
     * @return the province
     */
    public String getProvinceOrState() {
        return provinceOrState;
    }

    /**
     * The setter for the province
     *
     * @param provinceOrState the province
     */
    public void setProvinceOrState(String provinceOrState) {
        this.provinceOrState = provinceOrState;
    }

    /**
     * The getter for the city.
     *
     * @return the city.
     */
    public String getCity() {
        return city;
    }

    /**
     * The setter for the city.
     *
     * @param city the city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * The getter for the address
     *
     * @return the address.
     */
    public String getAddress() {
        return address;
    }

    /**
     * The setter for the address
     *
     * @param address the address
     */
    public void setAddress(String address) {
        this.address = address;
    }
}