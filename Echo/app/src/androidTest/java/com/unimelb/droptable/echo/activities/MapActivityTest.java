//package com.unimelb.droptable.echo.activities;
//
//import android.support.test.rule.ActivityTestRule;
//import android.support.test.runner.AndroidJUnit4;
//
//import com.unimelb.droptable.echo.ClientInfo;
//import com.unimelb.droptable.echo.R;
//
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//
//import static android.support.test.espresso.Espresso.onView;
//import static android.support.test.espresso.action.ViewActions.click;
//import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
//import static android.support.test.espresso.action.ViewActions.typeText;
//import static android.support.test.espresso.matcher.ViewMatchers.withId;
//import static org.junit.Assert.*;
//
//@RunWith(AndroidJUnit4.class)
//public class MapActivityTest {
//
//    @Rule
//    public ActivityTestRule<MapActivity> mActivityRule =
//            new ActivityTestRule<>(MapActivity.class);
//
//    private MapActivity mapActivity;
//
//    @Before
//    public void setUp() throws Exception {
//        mapActivity = mActivityRule.getActivity();
//    }
//
//    @Test
//    public void testAddTask() {
//
//        assertEquals(true, mapActivity.hasWindowFocus());
//        onView(withId(R.id.addTaskButton)).perform(click());
//        assertEquals(false, mapActivity.hasWindowFocus());
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        mapActivity = null;
//    }
//}