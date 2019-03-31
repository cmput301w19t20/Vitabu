package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)
public class BookInfoTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Activity activity;

    public BookInfoTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
        solo.clickOnText("The Two Towers");
    }

    @Test
    public void start() throws Exception {
        activity = rule.getActivity();
    }

    @Test
    public void testContent() {
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
        assertTrue(solo.waitForText("The Two Towers", 2, 2000));
        assertTrue(solo.waitForText("9780007203550", 1, 2000));
    }

    @Test
    public void testViewOwner() {
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
        solo.clickOnButton("View Owner");
        solo.assertCurrentActivity("Wrong Activity", userProfileActivity.class);
    }

    @Test
    public void testRequestBook() {
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
        solo.clickOnButton("Request Book");
        assertTrue(solo.waitForText("You have requested 'The Two Towers'"));
    }

    @Test
    public void testViewGoodreads() {
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
        solo.clickOnButton("View Goodreads Page");
        assertTrue(solo.getCurrentActivity() != activity);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
