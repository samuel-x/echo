package com.unimelb.droptable.echo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientInfoTest {

    private static final String TEST_USER_NAME = "AS&(8asdg";

    @Test
    public void testUsername() {
        ClientInfo.setUsername(TEST_USER_NAME);
        assertEquals(TEST_USER_NAME, ClientInfo.getUsername());
    }

    @Test
    public void testIsAssistant() {
        ClientInfo.setIsAssistant(false);
        assertEquals(false, ClientInfo.isAssistant());

        ClientInfo.setIsAssistant(true);
        assertEquals(true, ClientInfo.isAssistant());
    }

    @After
    public void tearDown() throws Exception {
        // Reset ClientInfo.
        ClientInfo.setIsAssistant(false);
        ClientInfo.setUsername(null);
    }
}