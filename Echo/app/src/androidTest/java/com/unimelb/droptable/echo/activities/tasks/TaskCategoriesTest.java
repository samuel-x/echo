package com.unimelb.droptable.echo.activities.tasks;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskCategories;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TaskCategoriesTest {

    @Rule
    public ActivityTestRule<TaskCategories> mActivityRule =
            new ActivityTestRule<>(TaskCategories.class);

    private TaskCategories taskCategory;

    @Before
    public void setUp() throws Exception {
        taskCategory = mActivityRule.getActivity();
    }

    @Test
    public void testTransportCategory() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskTransport)).perform(ViewActions.click());
        assertEquals(false, taskCategory.hasWindowFocus());
    }

    @Test
    public void testDeliveryCategory() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskDelivery)).perform(ViewActions.click());
        assertEquals(false, taskCategory.hasWindowFocus());
    }

    @Test
    public void testHouseholdCategory() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskHousehold)).perform(ViewActions.click());
        assertEquals(false, taskCategory.hasWindowFocus());
    }

    @Test
    public void testOtherCategory() {
        Espresso.onView(ViewMatchers.withId(R.id.buttonTaskOther)).perform(ViewActions.click());
        assertEquals(false, taskCategory.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {

    }
}