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

public class TaskSubcategoriesTest {

    @Rule
    public ActivityTestRule<TaskSubcategories> mActivityRule =
            new ActivityTestRule<>(TaskSubcategories.class);

    private TaskSubcategories taskSubcategories;

    @Before
    public void setUp() throws Exception {
        taskSubcategories = mActivityRule.getActivity();
    }

    @Test
    public void testButtonA() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskSubcategoryA)).perform(ViewActions.click());
        assertEquals(false, taskSubcategories.hasWindowFocus());
    }

    @Test
    public void testButtonB() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskSubcategoryB)).perform(ViewActions.click());
        assertEquals(false, taskSubcategories.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}