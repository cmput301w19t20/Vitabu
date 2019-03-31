package com.example.vitabu;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class LocationTest {

    // test Location class
    Location loc = new Location();

    @Test
    public void locCountry_isCorrect() {
        // test getters and setters
        loc.setCountry("Canada");
        assertEquals("Canada", loc.getCountry());
    }

    @Test
    public void locProvinceOrState_isCorrect() {
        // test getters and setters
        loc.setProvinceOrState("Alberta");
        assertEquals("Alberta", loc.getProvinceOrState());
    }

    @Test
    public void locCity_isCorrect() {
        // test getters and setters
        loc.setCity("Edmonton");
        assertEquals("Edmonton", loc.getCity());
    }

    @Test
    public void locAddress_isCorrect() {
        // test getters and setters
        loc.setAddress("UofA");
        assertEquals("UofA", loc.getAddress());
    }

    @Test
    public void locLat_isCorrect() {
        // test getters and setters
        loc.setLat(123.456);
        assertEquals(123.456, loc.getLat(),0.001);
    }

    @Test
    public void locLng_isCorrect() {
        // test getters and setters
        loc.setLng(654.321);
        assertEquals(654.321, loc.getLng(),0.001);
    }
}
