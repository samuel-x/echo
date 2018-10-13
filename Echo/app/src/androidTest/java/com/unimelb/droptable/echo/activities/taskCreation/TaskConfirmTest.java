package com.unimelb.droptable.echo.activities.taskCreation;

import android.support.test.espresso.action.ViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static org.junit.Assert.*;

public class TaskConfirmTest {
    @Rule
    public ActivityTestRule<TaskConfirm> mActivityRule =
            new ActivityTestRule<>(TaskConfirm.class);

    private TaskConfirm taskConfirm;

    @Before
    public void setUp() throws Exception {
        ClientInfo.setUsername("TEST");
        taskConfirm = mActivityRule.getActivity();
    }

    @Test
    public void testTaskCreation() {
        // Check initial conditions.
        Utility.currentTaskBuilder = ImmutableTask.builder()
                .title("Placeholder Title")
                .address("Placeholder Address")
                .category("Placeholder Category")
                .subCategory("Placeholder Subcategory")
                .notes("Placeholder Notes")
                .ap("Placeholder AP");
        ClientInfo.setUsername("TEST");
        assertEquals(true, taskConfirm.hasWindowFocus());
        assertNull(ClientInfo.getTask());

        // Perform action.
        onView(ViewMatchers.withId(R.id.buttonTaskConfirmConfirm)).perform(ViewActions.click());

        // Check final conditions.
        assertNotNull(ClientInfo.getTask());
        assertEquals(false, taskConfirm.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {
        // Reset task builder.
        Utility.currentTaskBuilder = ImmutableTask.builder()
                .title("Placeholder Title")
                .address("Placeholder Address")
                .category("Placeholder Category")
                .subCategory("Placeholder Subcategory")
                .notes("Placeholder Notes")
                .ap("Placeholder AP");
    }
}