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

import static android.provider.Settings.System.getString;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

@RunWith(AndroidJUnit4.class)
public class ApMapActivityTest {

    @Rule
    public ActivityTestRule<ApMapActivity> mActivityRule =
            new RelaunchActivityRule<>(ApMapActivity.class);



    private ApMapActivity apMapActivity;
    private Button taskButton;

    @Before
    public void setUp() throws Exception {
        apMapActivity = mActivityRule.getActivity();
        taskButton = apMapActivity.taskButton;
    }

    @Test
    public void testTaskButton() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.apTaskButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @Test
    public void testHelperButton() {
        assertEquals(true, apMapActivity.hasWindowFocus());
        onView(withId(R.id.apMapHelperButton)).perform(click());
        assertEquals(false, apMapActivity.hasWindowFocus());
    }

    @Test
    public void testTaskButtonTextOnTaskAssign() {

        assertEquals(apMapActivity.taskButton.getText(), "New Task");

//        String testTitle = "MyTitle";
//        String testAddress = "MyAddress";
//        String testCategory = "MyCategory";
//        String testSubCategory = "MySubCategory";
//        String testNotes = "MyNotes";
//        String testStatus = "MyStatus";
//        String ap = "AP";
//        String assistant = "assistant";
//        String id = "ID";
//
//        ImmutableTask task = ImmutableTask.builder()
//                .title(testTitle)
//                .address(testAddress)
//                .category(testCategory)
//                .subCategory(testSubCategory)
//                .notes(testNotes)
//                .status(testStatus)
//                .ap(ap)
//                .assistant(assistant)
//                .id(id).build();
//
//        ClientInfo.setTask(task);

//        mActivityRule.finishActivity();
//
//        assertEquals(apMapActivity.taskButton.getText(), "Current Task");
    }

    @After
    public void tearDown() throws Exception {
        apMapActivity = null;
    }
}