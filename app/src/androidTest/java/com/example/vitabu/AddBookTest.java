package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

public class AddBookTest extends ActivityTestRule<browseBooksActivity> {
    private Solo solo;

    public AddBookTest() {
        super(browseBooksActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<browseBooksActivity> rule =
            new ActivityTestRule<>(browseBooksActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
