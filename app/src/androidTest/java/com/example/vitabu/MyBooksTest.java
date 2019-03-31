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
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class MyBooksTest extends ActivityTestRule<MainActivity> {
    private Solo solo;

    public MyBooksTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testBook() {
        solo.clickOnText("My Books");
        assertTrue(solo.waitForText("The Two Towers", 1, 2000));
    }

    @Test
    public void testAvailableBook() {
        solo.clickOnText("My Books");
        solo.clickOnText("available", 2);
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookEditActivity.class);
    }

    @Test
    public void testBorrowedBook() {
        solo.clickOnText("My Books");
        solo.clickOnText("borrowed");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", returnBookActivity.class);
    }

    @Test
    public void testRequestedBook() {
        solo.clickOnText("My Books");
        solo.clickOnText("requested");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", acceptBookRequestActivity.class);
    }

    @Test
    public void testAcceptedBook() {
        solo.clickOnText("My Books");
        solo.clickOnText("accepted");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", acceptBookActivity.class);
    }


    @After
    public void tearDown() throws Exception {
        solo.finishOpenedActivities();
    }
}
