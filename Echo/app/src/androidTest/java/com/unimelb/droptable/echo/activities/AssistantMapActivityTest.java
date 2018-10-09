package com.unimelb.droptable.echo.activities;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.*;

//@RunWith(AndroidJUnit4.class)
//public class AssistantMapActivityTest {
//
//    @Rule
//    public ActivityTestRule<AssistantMapActivity> mActivityRule =
//            new ActivityTestRule<>(AssistantMapActivity.class);
//
//    private AssistantMapActivity assistantMapActivity;
//
//    @Before
//    public void setUp() throws Exception {
//        assistantMapActivity = mActivityRule.getActivity();
//    }
//
//    @After
//    public void tearDown() throws Exception {
//        ClientInfo.setUsername(null);
//    }
//}