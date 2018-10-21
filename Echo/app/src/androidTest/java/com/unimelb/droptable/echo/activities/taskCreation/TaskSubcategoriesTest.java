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

public class TaskSubcategoriesTest {

    @Rule
    public ActivityTestRule<TaskSubcategories> mActivityRule =
            new ActivityTestRule<>(TaskSubcategories.class);

    private TaskSubcategories taskSubcategories;

    @Before
    public void setUp() {
        taskSubcategories = mActivityRule.getActivity();
        taskSubcategories.getIntent().putExtra("category", "Household");
    }

    @Test
    public void testButtonA() {
        assertTrue(taskSubcategories.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonTaskSubcategoryA)).perform(ViewActions.click());
        assertFalse(taskSubcategories.hasWindowFocus());
    }

    @Test
    public void testButtonB() {
        assertTrue(taskSubcategories.hasWindowFocus());
        onView(ViewMatchers.withId(R.id.buttonTaskSubcategoryB)).perform(ViewActions.click());
        assertFalse(taskSubcategories.hasWindowFocus());
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