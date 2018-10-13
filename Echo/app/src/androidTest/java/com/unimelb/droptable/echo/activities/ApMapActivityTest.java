package com.unimelb.droptable.echo.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.widget.Button;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class ApMapActivityTest {

    @Rule
    public ActivityTestRule<ApMapActivity> mActivityRule =
            new ActivityTestRule<>(ApMapActivity.class);

    private ApMapActivity apMapActivity;

    private static final String TEST_TITLE = "Test TEST_TITLE";
    private static final String TEST_ADDRESS = "TEST_ADDRESS";
    private static final String TEST_NOTES = "TEST_NOTES";
    private static final String TEST_STATUS = "TEST_STATUS";
    private static final String TEST_AP = "TEST_AP";
    private static final String TEST_ASSISTANT = "TEST_ASSISTANT";

    @Before
    public void setUp() throws Exception {
        apMapActivity = mActivityRule.getActivity();
    }

    @Test
    public void testTaskButtonTextOnTaskAssign() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        assertEquals(apMapActivity.taskButton.getText().toString(), "New Task");
        // TODO: Implement a full test for checking whether the text changes on task assignment
    }

    @Test
    public void testTaskButton() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.apTaskButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @Test
    public void testHelperButton() {
        ImmutableTask task = ImmutableTask.builder()
                .title(TEST_TITLE)
                .address(TEST_ADDRESS)
                .notes(TEST_NOTES)
                .status(TEST_STATUS)
                .ap(TEST_AP)
                .assistant(TEST_ASSISTANT)
                .build();
        ClientInfo.setUsername("test");
        ClientInfo.setCurrentToken("test");
        ClientInfo.setTask(task);
        ClientInfo.setIsAssistant(false);
        ClientInfo.setPhoneNumber("0412356789");
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.apMapHelperButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @After
    public void tearDown() throws Exception {
        apMapActivity = null;
        ClientInfo.resetClientInfo();
    }
}