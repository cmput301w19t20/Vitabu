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
        solo = new Solo(getInstrumentation(), rule.getActivity());
        solo.clickOnView(solo.getView(R.id.browse_books_appbar_search));
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testActivity() {
        solo.assertCurrentActivity("Wrong Activity", searchBooksActivity.class);
    }

    @Test
    public void testAllEmpty() {
        solo.clickOnView(solo.getView(R.id.search_books_search_button));
        assertTrue(solo.waitForText("Search returned no results."));
    }

    @Test
    public void testAuthor() {
        solo.enterText((EditText) solo.getView(R.id.search_books_author_edittext), "JRR Tolkien");
        solo.clickOnView(solo.getView(R.id.search_books_search_button));
        solo.assertCurrentActivity("Wrong activity", searchBookResultsActivity.class);
    }

    @Test
    public void testTitle() {
        solo.enterText((EditText) solo.getView(R.id.search_books_title_edittext), "The Two Towers");
        solo.clickOnView(solo.getView(R.id.search_books_search_button));
        solo.assertCurrentActivity("Wrong activity", searchBookResultsActivity.class);
    }

    @Test
    public void testISBN() {
        solo.enterText((EditText) solo.getView(R.id.search_books_isbn_edittext), "9780007203550");
        solo.clickOnView(solo.getView(R.id.search_books_search_button));
        solo.assertCurrentActivity("Wrong activity", searchBookResultsActivity.class);
    }

    @Test
    public void testKeywords() {
        solo.enterText((EditText) solo.getView(R.id.search_books_keywords_edittext), "second book in lotr");
        solo.clickOnView(solo.getView(R.id.search_books_search_button));
        solo.assertCurrentActivity("Wrong activity", searchBookResultsActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
