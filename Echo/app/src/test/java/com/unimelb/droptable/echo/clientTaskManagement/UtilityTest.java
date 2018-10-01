package com.unimelb.droptable.echo.clientTaskManagement;

import org.junit.Test;

import static org.junit.Assert.*;

public class UtilityTest {
    @Test
    public void testGenerateUserChatId() {
        String user1 = "ABCD";
        String user2 = "EFGH";

        assertEquals("ABCD-EFGH", Utility.generateUserChatId(user1, user2));
        assertEquals("ABCD-EFGH", Utility.generateUserChatId(user2, user1));
    }
}