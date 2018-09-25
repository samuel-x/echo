package com.unimelb.droptable.echo.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ApMapActivityTest {

    @Rule
    public ActivityTestRule<ApMapActivity> mActivityRule =
            new ActivityTestRule<>(ApMapActivity.class);

    private ApMapActivity apMapActivity;

    @Before
    public void setUp() throws Exception {
        apMapActivity = mActivityRule.getActivity();
    }

    @Test
    public void testTaskButton() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.taskButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @Test
    public void testHelperButton() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.apMapHelperButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {
        apMapActivity = null;
    }
}