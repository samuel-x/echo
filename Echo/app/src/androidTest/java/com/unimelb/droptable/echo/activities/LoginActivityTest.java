package com.unimelb.droptable.echo.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(AndroidJUnit4.class)
public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> mActivityRule =
            new ActivityTestRule<>(LoginActivity.class);

    private static String VALID_USERNAME = "Ah*&S46";
    private static String VALID_PHONE = "0412356789";
    private static String VALID_ASSISTANT = "assistant";
    private static String INVALID_USERNAME = "Ha";
    private static String INVALID_PHONE = "0";

    private LoginActivity loginActivity;

    @Before
    public void setUp() {
        loginActivity = mActivityRule.getActivity();
    }

    @Test
    public void testSuccessfulLogin() {
        onView(withId(R.id.usernameText)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberText)).perform(typeText(VALID_PHONE), closeSoftKeyboard());
        onView(withId(R.id.signInButton)).perform(click());
        assertEquals(VALID_USERNAME, ClientInfo.getUsername());
        assertEquals(false, loginActivity.hasWindowFocus());
    }

    @Test
    public void testUnsuccessfulLogin() {
        onView(withId(R.id.usernameText)).perform(typeText(INVALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberText)).perform(typeText(INVALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.signInButton)).perform(click());
        assertEquals(null, ClientInfo.getUsername());
        assertEquals(true, loginActivity.hasWindowFocus());
    }

    @Test
    public void testAssistantChecked() {
        onView(withId(R.id.usernameText)).perform(typeText(VALID_ASSISTANT), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberText)).perform(typeText(VALID_PHONE), closeSoftKeyboard());
        onView(withId(R.id.isAssistantCheckBox)).perform(click());
        onView(withId(R.id.signInButton)).perform(click());
        assertEquals(true, ClientInfo.isAssistant());
    }

    @Test
    public void testAssistantNotChecked() {
        onView(withId(R.id.usernameText)).perform(typeText(VALID_USERNAME), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberText)).perform(typeText(VALID_PHONE), closeSoftKeyboard());
        onView(withId(R.id.isAssistantCheckBox)).perform(click());
        assertEquals(false, ClientInfo.isAssistant());
    }

    @Test
    public void testHelperButton() {
        assertEquals(true, loginActivity.hasWindowFocus());
        onView(withId(R.id.loginHelperButton)).perform(click());
        assertEquals(false, loginActivity.hasWindowFocus());
    }

    @Test
    public void testToken() {
        onView(withId(R.id.usernameText)).perform(typeText(VALID_ASSISTANT), closeSoftKeyboard());
        onView(withId(R.id.phoneNumberText)).perform(typeText(VALID_PHONE), closeSoftKeyboard());
        onView(withId(R.id.signInButton)).perform(click());
        assertNotEquals(null, ClientInfo.getToken());
    }

    @After
    public void tearDown() {
        loginActivity = null;

        // Reset ClientInfo.
        ClientInfo.setIsAssistant(false);
        ClientInfo.setUsername(null);
    }
}