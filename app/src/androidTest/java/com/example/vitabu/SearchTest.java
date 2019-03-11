package com.example.vitabu;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;

@RunWith(AndroidJUnit4.class)
public class SearchTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Date date;

    public SearchTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
//        Login
        solo = new Solo(getInstrumentation(), rule.getActivity());
//        solo.assertCurrentActivity("Wrong Activity", browseBooksActivity.class);
//        solo.clickOnText("Add Book");
        date = new Date();
    }
}
