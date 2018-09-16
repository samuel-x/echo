package com.unimelb.droptable.echo.activities.tasks;

import android.support.test.rule.ActivityTestRule;

import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

public class TaskConfirmTest {

    @Rule
    public ActivityTestRule<TaskConfirm> mActivityRule =
            new ActivityTestRule<>(TaskConfirm.class);

    private TaskConfirm taskConfirm;

    @Before
    public void setUp() throws Exception {
        taskConfirm = mActivityRule.getActivity();
    }

    @Test
    public void testConfirm() {
        assertEquals(true, taskConfirm.hasWindowFocus());
        onView(withId(R.id.buttonTaskConfirmConfirm)).perform(click());
        assertEquals(false, taskConfirm.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}