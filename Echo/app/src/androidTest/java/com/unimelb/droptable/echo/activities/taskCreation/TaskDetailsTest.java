package com.unimelb.droptable.echo.activities.taskCreation;

import android.support.test.rule.ActivityTestRule;
import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
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
    private final String TEST_NOTES = "Test Text";

    @Before
    public void setUp() throws Exception {
        taskDetails = mActivityRule.getActivity();
    }

    @Test
    public void testFormFillIn() {
        assertEquals(true, taskDetails.hasWindowFocus());
        onView(withId(R.id.textTaskTitle)).perform(typeText(TEST_STR), closeSoftKeyboard());
        onView(withId(R.id.textTaskNotes)).perform(typeText(TEST_NOTES), closeSoftKeyboard());
        onView(withId(R.id.buttonTaskNow)).perform(click());
        assertEquals(false, taskDetails.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}