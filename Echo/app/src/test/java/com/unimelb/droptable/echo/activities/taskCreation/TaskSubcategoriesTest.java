package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, TaskSubcategories.class})
public class TaskSubcategoriesTest {

    private static String TEST_A_TEXT = "TESTA";
    private static String TEST_B_TEXT = "TESTB";

    private TaskSubcategories taskSubCategories;
    private Intent intentMock;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskSubCategories = Mockito.spy(TaskSubcategories.class);
        intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskSubCategories).finish();
        Mockito.doNothing().when(taskSubCategories).startActivity(any(Intent.class));

        // Mock task builder.
        Utility.currentTaskBuilder = PowerMockito.mock(ImmutableTask.Builder.class);

        // Mock buttons.
        taskSubCategories.subcategoryA = PowerMockito.mock(Button.class);
        when(taskSubCategories.subcategoryA.getText()).thenReturn(TEST_A_TEXT);
        taskSubCategories.subcategoryB = PowerMockito.mock(Button.class);
        when(taskSubCategories.subcategoryB.getText()).thenReturn(TEST_B_TEXT);
    }

    @After
    public void tearDown() throws Exception {
        Utility.currentTaskBuilder = null;
    }

    @Test
    public void taskDetailsA() throws Exception {
        // Verify prior.
        verify(Utility.currentTaskBuilder, times(0))
                .subCategory(TEST_A_TEXT);
        verify(taskSubCategories, times(0)).startActivity(intentMock);

        // Execute.
        taskSubCategories.taskDetailsA();

        // Verify post.
        verify(Utility.currentTaskBuilder, times(1))
                .subCategory(TEST_A_TEXT);
        verify(taskSubCategories, times(1)).startActivity(intentMock);
        PowerMockito.verifyNew(Intent.class).withArguments(taskSubCategories, TaskDetails.class);
    }

    @Test
    public void taskDetailsB() throws Exception {
        // Verify prior.
        verify(Utility.currentTaskBuilder, times(0))
                .subCategory(TEST_B_TEXT);
        verify(taskSubCategories, times(0)).startActivity(intentMock);

        // Execute.
        taskSubCategories.taskDetailsB();

        // Verify post.
        verify(Utility.currentTaskBuilder, times(1))
                .subCategory(TEST_B_TEXT);
        verify(taskSubCategories, times(1)).startActivity(intentMock);
        PowerMockito.verifyNew(Intent.class).withArguments(taskSubCategories, TaskDetails.class);
    }
}