package com.example.vitabu;

/**
 * @author davidowe
 * @version 1.0
 * A Location object to keep track of country, province, city, and (optionally) address.
 */
public class Location {
    private String country;
    private String provinceOrState;
    private String city;
    private String address;

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