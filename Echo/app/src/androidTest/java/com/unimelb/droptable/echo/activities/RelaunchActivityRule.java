package com.unimelb.droptable.echo.activities;

import android.app.Activity;
import android.support.test.rule.ActivityTestRule;

public class RelaunchActivityRule<T extends Activity> extends ActivityTestRule<T> {

    public RelaunchActivityRule(Class<T> activityClass) {
        super(activityClass,false);
    }

    public RelaunchActivityRule(Class<T> activityClass, boolean initialTouchMode) {
        super(activityClass, initialTouchMode,true);
    }

    public RelaunchActivityRule(Class<T> activityClass, boolean initialTouchMode,
                                boolean launchActivity) {
        super(activityClass, initialTouchMode, launchActivity);
    }

    @Override
    protected void afterActivityFinished() {
        super.afterActivityFinished();
        launchActivity(getActivityIntent());
    }
}
