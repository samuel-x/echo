package com.unimelb.droptable.echo;

import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

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

    @Test
    public void testAssignTask() {
        String testTitle = "MyTitle";
        String testAddress = "MyAddress";
        String testCategory = "MyCategory";
        String testSubCategory = "MySubCategory";
        String testNotes = "MyNotes";
        String testStatus = "MyStatus";
        String ap = "AP";
        String assistant = "assistant";
        String id = "ID";

        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .notes(testNotes)
                .status(testStatus)
                .ap(ap)
                .assistant(assistant)
                .id(id).build();

        ClientInfo.setTask(task);
        assertEquals(ClientInfo.getTask(), task);
    }

    @Test
    public void testToken() {
        String testToken = "ABC";

        ClientInfo.setCurrentToken(testToken);

        assertEquals(ClientInfo.getToken(), testToken);
    }

    @After
    public void tearDown() throws Exception {
        // Reset ClientInfo.
        ClientInfo.setIsAssistant(false);
        ClientInfo.setUsername(null);
    }
}