package com.unimelb.droptable.echo.activities.taskCreation;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.assertEquals;

public class TaskCreationTest {

    @Rule
    public ActivityTestRule<TaskCreation> mActivityRule =
            new ActivityTestRule<>(TaskCreation.class);

    private TaskCreation taskCreation;

    @Before
    public void setUp() throws Exception {
        taskCreation = mActivityRule.getActivity();
    }

    @Test
    public void testNewTask() {
        assertEquals(true, taskCreation.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonNewTask)).perform(ViewActions.click());
        assertEquals(false, taskCreation.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}