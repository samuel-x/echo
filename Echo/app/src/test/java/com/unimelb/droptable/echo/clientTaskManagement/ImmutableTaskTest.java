package com.unimelb.droptable.echo.clientTaskManagement;

import org.junit.Test;

import static org.junit.Assert.*;

public class ImmutableTaskTest {

    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testNotes = "MyNotes";

    @Test
    public void testBuilder() {
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .notes(testNotes)
                .build();

        assertEquals(testTitle, task.getTitle());
        assertEquals(testAddress, task.getAddress());
        assertEquals(testCategory, task.getCategory());
        assertEquals(testSubCategory, task.getSubCategory());
        assertEquals(testNotes, task.getNotes());
    }
}