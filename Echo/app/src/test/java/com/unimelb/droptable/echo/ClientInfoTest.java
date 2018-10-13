package com.unimelb.droptable.echo;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.After;
import org.junit.Test;

import static org.junit.Assert.*;

public class ClientInfoTest {

    private final String TEST_USER_NAME = "AS&(8asdg";
    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testStatus = "MyStatus";
    private final String testAp = "MyAp";
    private final String testAssistant1 = "MyAssistant1";
    private final String testAssistant2 = "MyAssistant2";
    private final String testNotes = "MyNotes";
    private static final String TEST_PHONE_NUMBER = "0412356789";

    @Test
    public void testUsername() {
        ClientInfo.setUsername(TEST_USER_NAME);
        assertEquals(TEST_USER_NAME, ClientInfo.getUsername());
    }

    @Test
    public void testPhoneNumber() {
        ClientInfo.setPhoneNumber(TEST_PHONE_NUMBER);
        assertEquals(TEST_PHONE_NUMBER, ClientInfo.getPhoneNumber());
    }

    @Test
    public void testIsAssistant() {
        ClientInfo.setIsAssistant(false);
        assertEquals(false, ClientInfo.isAssistant());

        ClientInfo.setIsAssistant(true);
        assertEquals(true, ClientInfo.isAssistant());
    }

    @Test
    public void apUnacceptedHasPartner() {
        // Create fake, unaccepted task.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .notes(testNotes)
                .build();

        // Setup prior.
        ClientInfo.setTask(task);
        ClientInfo.setIsAssistant(false);

        // Make assertion.
        assertFalse(ClientInfo.hasPartner());
    }

    @Test
    public void apAcceptedHasPartner() {
        // Create fake, accepted task.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .assistant(testAssistant1)
                .notes(testNotes)
                .build();

        // Setup prior.
        ClientInfo.setTask(task);
        ClientInfo.setIsAssistant(false);

        // Make assertion.
        assertTrue(ClientInfo.hasPartner());
    }

    @Test
    public void assistantHasPartner() {
        // Create fake task.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .assistant(testAssistant1)
                .notes(testNotes)
                .build();

        // Setup prior.
        ClientInfo.setTask(task);
        ClientInfo.setIsAssistant(true);

        // Make assertion.
        assertTrue(ClientInfo.hasPartner());
    }

    @Test
    public void updateAssistant() {
        // Create fake task.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .assistant(testAssistant1)
                .notes(testNotes)
                .build();

        // Setup prior.
        ClientInfo.setTask(task);
        ClientInfo.setIsAssistant(false);

        // Verify prior.
        assertEquals(testAssistant1, ClientInfo.getTask().getAssistant());

        // Execute update.
        ClientInfo.updateAssistant(testAssistant2);

        // Verify post.
        assertEquals(testAssistant2, ClientInfo.getTask().getAssistant());
    }

    @After
    public void tearDown() throws Exception {
        // Reset ClientInfo.
        ClientInfo.setIsAssistant(false);
        ClientInfo.setUsername(null);
    }
}