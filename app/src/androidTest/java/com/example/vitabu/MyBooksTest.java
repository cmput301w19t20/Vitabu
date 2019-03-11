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
        assertTrue(solo.waitForText("TestBook", 1, 2000));
        assertTrue(solo.waitForText("TestAuthor", 1, 2000));
    }

    @Test
    public void testBookClick() {
        solo.clickOnText("My Books");
        solo.clickOnText("Title:");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
