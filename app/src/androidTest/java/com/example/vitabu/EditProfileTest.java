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
public class EditProfileTest extends ActivityTestRule<MainActivity> {
    private Solo solo;

    public EditProfileTest() {
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
    public void testContent() {
        solo.waitForActivity(browseBooksActivity.class);
        solo.clickOnActionBarItem(R.menu.browse_books_appbar);
        solo.clickOnMenuItem("My Profile");
        assertTrue(solo.waitForText("testusername1", 1, 2000));
        assertTrue(solo.waitForText("test1@email.com", 1, 2000));
        assertTrue(solo.waitForText("Edmonton", 1, 2000));
    }

    @Test
    public void testSeeUserReviews() {
        solo.waitForActivity(browseBooksActivity.class);
        solo.clickOnActionBarItem(R.menu.browse_books_appbar);
        solo.clickOnMenuItem("My Profile");
        solo.clickOnButton("See User Reviews");
        solo.assertCurrentActivity("Wrong Activity", userReviewActivity.class);
    }

    @Test
    public void testSave() {
        solo.waitForActivity(browseBooksActivity.class);
        solo.clickOnActionBarItem(R.menu.browse_books_appbar);
        solo.clickOnMenuItem("My Profile");
        solo.clickOnButton("Save");
        solo.assertCurrentActivity("Wrong Activity", browseBooksActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
