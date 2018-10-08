package com.unimelb.droptable.echo.clientTaskManagement;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableTaskTest {

    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testNotes = "MyNotes";
    private final String ap = "AP";
    private final String testStatus = "MyStatus";
    private final String assistant = "assistant";
    private final String id = "ID";

    @Test
    public void testBuilder() {
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

        assertEquals(testTitle, task.getTitle());
        assertEquals(testAddress, task.getAddress());
        assertEquals(testCategory, task.getCategory());
        assertEquals(testSubCategory, task.getSubCategory());
        assertEquals(testNotes, task.getNotes());
        assertEquals(ap, task.getAp());
        assertEquals(testStatus, task.getStatus());
        assertEquals(assistant, task.getAssistant());
        assertEquals(id, task.getId());
    }
}