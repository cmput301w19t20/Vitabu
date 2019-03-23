package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.vitabu.MainActivity;
import com.example.vitabu.bookInfoActivity;
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
public class RequestsTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Date date;

    public RequestsTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
        date = new Date();
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testActivity() {
        solo.clickOnText("Requests");
//        solo.assertCurrentActivity("Wrong Activity", browseBooksActivity.class);
    }

    @Test
    public void testBook() {
        solo.clickOnText("Requests");
        assertTrue(solo.waitForText("temp3", 1, 2000));
        assertTrue(solo.waitForText("temp3", 1, 2000));
    }

    @Test
    public void testBookClick() {
        solo.clickOnText("Requests");
        solo.clickOnText("temp3");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}