package com.unimelb.droptable.echo.clientTaskManagement;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class FirebaseAdapterTest {

    @Test
    public void testPush() {
        Task testTask = new Task("title", "address", "notes");
        FirebaseAdapter.pushTask(testTask);
    }
}