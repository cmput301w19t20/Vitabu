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
    private double lat;
    private double lng;

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

    /**
     * The getter for the lat.
     *
     * @return the lat
     */
    public double getLat() {
        return lat;
    }

    /**
     * The setter for the lat.
     *
     * @param lat the latitude
     */
    public void setLat(double lat) {
        this.lat = lat;
    }

    /**
     * The getter for the lat.
     *
     * @return the lat
     */
    public double getLng() {
        return lng;
    }

    /**
     * The setter for the lat.
     *
     * @param lng the latitude
     */
    public void setLng(double lng) {
        this.lng = lng;
    }
}