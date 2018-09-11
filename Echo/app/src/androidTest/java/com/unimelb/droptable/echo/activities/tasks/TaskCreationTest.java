package com.unimelb.droptable.echo.activities.tasks;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskCreation;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

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
        Espresso.onView(ViewMatchers.withId(R.id.buttonNewTask)).perform(ViewActions.click());
        assertEquals(false, taskCreation.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}