package com.example.vitabu;

import android.app.Activity;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.rule.ActivityTestRule;
import android.widget.EditText;

import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static junit.framework.TestCase.assertTrue;

@RunWith(AndroidJUnit4.class)
public class LoginTest extends ActivityTestRule<MainActivity> {
    private Solo solo;

    public LoginTest() {
        super(MainActivity.class, false, true);
    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, false, true);

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
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void testEmptyPassword() {
//        Empty password
        solo.enterText((EditText) solo.getView(R.id.login_email), "arseniykd@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForText("The email and password fields cannot be empty."));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void testWrongPassword() {
//        Wrong password
        solo.enterText((EditText) solo.getView(R.id.login_email), "arseniykd@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "wrongpass");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForText("Sign In failed.", 1, 2000));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void testEmptyEmail() {
//        Empty email
        solo.enterText((EditText) solo.getView(R.id.login_email), "");
        solo.enterText((EditText) solo.getView(R.id.login_password), "qwertyui");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForText("The email and password fields cannot be empty.", 1, 2000));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void testWrongEmail() {
//        Wrong email
        solo.enterText((EditText) solo.getView(R.id.login_email), "wrongemail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "qwertyui");
        solo.clickOnButton("Login");
        assertTrue(solo.waitForText("Sign In failed.", 1, 2000));
        solo.assertCurrentActivity("Wrong Activity", MainActivity.class);
    }

    @Test
    public void testLogin() {
//        Correct email and password
        solo.enterText((EditText) solo.getView(R.id.login_email), "arseniykd@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.login_password), "qwertyui");
        solo.clickOnButton("Login");
        solo.assertCurrentActivity("Wrong Activity", browseBooksActivity.class);
    }

    @Test
    public void testRegister() {
        solo.clickOnButton("Register");
        solo.assertCurrentActivity("Wrong Activity", registerActivity.class);
    }

    @After
    public void tearDown() throws Exception{
        solo.finishOpenedActivities();
    }
}
