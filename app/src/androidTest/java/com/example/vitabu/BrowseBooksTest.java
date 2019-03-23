package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.EditText;

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
public class BrowseBooksTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Date date;

    public BrowseBooksTest() {
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
        solo.clickOnButton("Browse");
//        solo.assertCurrentActivity("Wrong Activity", browseBooksActivity.class);
    }

    @Test
    public void testBook() {
        assertTrue(solo.waitForText("temp3", 1, 2000));
        assertTrue(solo.waitForText("temp3", 1, 2000));
    }

    @Test
    public void testBookClick() {
        solo.clickOnText("Title:");
        solo.assertCurrentActivity("Wrong Activity (check that there are books in database to click).", bookInfoActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
