package com.unimelb.droptable.echo.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

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
import static org.mockito.Mockito.mock;

public class ApMapActivityTest {

    ApMapActivity apMapActivity;

    @Before
    public void setUp() {
        apMapActivity = new ApMapActivity();
    }

    @Test
    public void createListener() {
        /**
         * Here, mocking the variables inside the listener created alters the hashed result
         * when asserting equals, so the best we can do is just check the type.
         */
        assertEquals(apMapActivity.createListener(), instanceOf(ChildEventListener.class));
    }

    @After
    public void tearDown() {
        apMapActivity = null;
    }
}