package com.unimelb.droptable.echo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Build;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;

public class AssistantMapActivityTest {

    AssistantMapActivity assistantMapActivity;

    @Before
    public void setUp() {
        assistantMapActivity = new AssistantMapActivity();
    }

    @Test
    public void createListener() {
        /**
         * Here, mocking the variables inside the listener created alters the hashed result
         * when asserting equals, so the best we can do is just check the type.
         */
        assertEquals(assistantMapActivity.createListener(), instanceOf(ChildEventListener.class));
    }

    @After
    public void tearDown() {
        assistantMapActivity = null;
    }
}