package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.Date;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

public class AddBookTest extends ActivityTestRule<MainActivity> {
    private Solo solo;
    private Date date;

    public AddBookTest() {
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

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testEmptyTitle() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.enterText((EditText) solo.getView(R.id.add_book_author_input), ("TestAuthor" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_isbn_input), Integer.toString(((int) date.getTime())));
        solo.enterText((EditText) solo.getView(R.id.add_book_description_input), ("Description for date: " + date));
        solo.clickOnButton("Add Book");
        assertTrue(solo.waitForText("Please, provide a title.", 1, 2000));
    }

    @Test
    public void testEmptyAuthor() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.enterText((EditText) solo.getView(R.id.add_book_title_input), ("TestBook" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_isbn_input), Integer.toString(((int) date.getTime())));
        solo.enterText((EditText) solo.getView(R.id.add_book_description_input), ("Description for date: " + date));
        solo.clickOnButton("Add Book");
        assertTrue(solo.waitForText("Please, provide an author.", 1, 2000));
    }

    @Test
    public void testEmptyISBN() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.enterText((EditText) solo.getView(R.id.add_book_title_input), ("TestBook" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_author_input), ("TestAuthor" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_description_input), ("Description for date: " + date));
        solo.clickOnButton("Add Book");
        assertTrue(solo.waitForText("Please, provide an ISBN.", 1, 2000));
    }

    @Test
    public void testScanISBN() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.clickOnButton("Scan ISBN");
        solo.assertCurrentActivity("Wrong Activity", ISBNActivity.class);
    }

    @Test
    public void testAddImage() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.clickOnButton("Add Image");
    }

    @Test
    public void testValidInput() {
        solo.clickOnText("Add Book");
        assertTrue(solo.waitForText("Add Book", 2, 2000));
        solo.enterText((EditText) solo.getView(R.id.add_book_title_input), ("TestBook" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_author_input), ("TestAuthor" + date));
        solo.enterText((EditText) solo.getView(R.id.add_book_isbn_input), Integer.toString(((int) date.getTime())));
        solo.enterText((EditText) solo.getView(R.id.add_book_description_input), ("Description for date: " + date));
        solo.clickOnButton("Add Book");
        assertTrue(solo.waitForText("Book Added.", 1, 2000));
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
