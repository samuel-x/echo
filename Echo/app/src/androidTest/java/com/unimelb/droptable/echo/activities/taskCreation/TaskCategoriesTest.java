package com.unimelb.droptable.echo.activities.taskCreation;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TaskCategoriesTest {

    @Rule
    public ActivityTestRule<TaskCategories> mActivityRule =
            new ActivityTestRule<>(TaskCategories.class);

    private TaskCategories taskCategory;

    @Before
    public void setUp() {
        taskCategory = mActivityRule.getActivity();
    }

    @Test
    public void testTransportCategory() {
        assertTrue(taskCategory.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonTaskTransport)).perform(ViewActions.click());
        assertFalse(taskCategory.hasWindowFocus());
    }

    @Test
    public void testDeliveryCategory() {
        assertTrue(taskCategory.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonTaskDelivery)).perform(ViewActions.click());
        assertFalse(taskCategory.hasWindowFocus());
    }

    @Test
    public void testHouseholdCategory() {
        assertTrue(taskCategory.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonTaskHousehold)).perform(ViewActions.click());
        assertFalse(taskCategory.hasWindowFocus());
    }

    @After
    public void tearDown() {
        // Reset task builder.
        Utility.currentTaskBuilder = ImmutableTask.builder()
                .title("Placeholder Title")
                .address("Placeholder Address")
                .category("Placeholder Category")
                .subCategory("Placeholder Subcategory")
                .notes("Placeholder Notes");
    }
}