package com.example.vitabu;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

public class RegisterTest extends ActivityTestRule<MainActivity> {
    private Solo solo;

    public RegisterTest() {
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
    public void testRegister() {
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity", registerActivity.class);

//        No username
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a full username.", 1, 2000));

//        Username already taken
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_username), "Tristan");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Username Is taken.", 1, 2000));

//        No email
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a valid email.", 1, 2000));

//        Email already taken
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "arseniykd@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("That email has been taken.", 1, 2000));

//        Invalid email
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "testemail");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a valid email.", 1, 2000));

//        No password
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a password that is more than 8 characters long."));

//        Password too short
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "pw");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a password that is more than 8 characters long."));

//        No country
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a country."));

//        No province
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a province.."));

//        No City
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a city."));

//        Valid input
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_country), "Canada");
        solo.enterText((EditText) solo.getView(R.id.register_province), "BC");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity.", browseBooksActivity.class);
    }


    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
