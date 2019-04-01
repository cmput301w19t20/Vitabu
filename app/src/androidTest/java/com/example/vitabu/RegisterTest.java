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
public class RegisterTest extends ActivityTestRule<registerActivity> {
    private Solo solo;
    private Date date;

    public RegisterTest() {
        super(registerActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<registerActivity> rule =
            new ActivityTestRule<>(registerActivity.class, false, true);

    @Before
    public void setUp() throws Exception {
        solo = new Solo(getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    @Test
    public void testActivity() {
        solo.assertCurrentActivity("Wrong Activity", registerActivity.class);
    }

    @Test
    public void testEmptyUsername() {
//        No username
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a full username.", 1, 2000));
    }

    @Test
    public void testTakenUsername() {
//        Username already taken
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_username), "Tristan");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Username Is taken.", 1, 2000));
    }

    @Test
    public void testEmptyEmail() {
//        No email
        solo.enterText((EditText) solo.getView(R.id.register_username), "testEmptyEmail");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a valid email.", 1, 2000));
    }

    @Test
    public void testTakenEmail() {
//        Email already taken
        solo.enterText((EditText) solo.getView(R.id.register_username), "testTakenEmail" + date.toString());
        solo.enterText((EditText) solo.getView(R.id.register_email), "jjpaton@ualberta.ca");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("That email has been taken.", 1, 2000));
    }

    @Test
    public void testInvalidEmail() {
//        Invalid email
        solo.enterText((EditText) solo.getView(R.id.register_username), "testInvalidEmail");
        solo.enterText((EditText) solo.getView(R.id.register_email), "testemail");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("That email has been taken.", 1, 2000));
    }

    @Test
    public void testEmptyPassword() {
//        No password
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a password that is at least 8 characters long."));
    }

    @Test
    public void testNotMatchingPassword() {
//        Passwords don't match
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "doesnotmatch");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, type in the same password in both the Password and Re-enter Password fields.", 1, 2000));
        solo.assertCurrentActivity("Wrong Activity.", registerActivity.class);
    }

    @Test
    public void testTooShortPassword() {
//        Password too short
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "pw");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "pw");
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a password that is at least 8 characters long.", 1, 2000));
        solo.assertCurrentActivity("Wrong Activity.", registerActivity.class);
    }

//    @Test
//    public void testEmptyCountry() {
////        No country
//        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
//        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
//        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
//        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
//        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
//        solo.clickOnButton("Register");
//        assertTrue(solo.waitForText("Please, provide a country."));
//    }

//    @Test
//    public void testEmptyProvince() {
////        No province
//        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
//        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
//        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
//        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
//        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
//        solo.clickOnButton("Register");
//        assertTrue(solo.waitForText("Please, provide a province.."));
//    }

    @Test
    public void testEmptyCity() {
//        No City
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername");
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com");
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword");
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword");
        solo.clickOnButton("Register");
        assertTrue(solo.waitForText("Please, provide a city.", 1, 2000));
    }

    @Test
    public void testRegister() {
//        Valid input
        solo.enterText((EditText) solo.getView(R.id.register_username), "testusername" + date.toString());
        solo.enterText((EditText) solo.getView(R.id.register_email), "test@email.com" + date.toString());
        solo.enterText((EditText) solo.getView(R.id.register_password), "testpassword" + date.toString());
        solo.enterText((EditText) solo.getView(R.id.register_reenter_password), "testpassword" + date.toString());
        solo.enterText((EditText) solo.getView(R.id.register_city), "Surrey");
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity.", MainActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
