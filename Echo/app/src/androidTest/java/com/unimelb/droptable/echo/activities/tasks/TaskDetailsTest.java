package com.unimelb.droptable.echo.activities.tasks;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.view.View;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class TaskDetailsTest {

    @Rule
    public ActivityTestRule<TaskDetails> mActivityRule =
            new ActivityTestRule<>(TaskDetails.class);

    private TaskDetails taskDetails;

    private final String TEST_STR = "Test Title";
    private final String TEST_NOTES = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
            "Aenean tempor lorem sit amet erat cursus, id suscipit enim dictum. Quisque in magna " +
            "blandit, fringilla ante a, ultricies magna. Nullam a mauris elementum, imperdiet " +
            "felis quis, semper ipsum. Proin rhoncus feugiat tortor vitae elementum. ";

    @Before
    public void setUp() throws Exception {
        taskDetails = mActivityRule.getActivity();
    }

    @Test
    public void testFormFillIn() {
        Espresso.onView(withId(R.id.textTaskTitle)).perform(typeText(TEST_STR), closeSoftKeyboard());
        Espresso.onView(withId(R.id.textTaskNotes)).perform(typeText(TEST_NOTES), closeSoftKeyboard());
        Espresso.onView(withId(R.id.buttonTaskNow)).perform(click());
        assertEquals(false, taskDetails.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}