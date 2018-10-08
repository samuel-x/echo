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
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, TaskCategories.class})
public class TaskSubcategoriesTest {

    private static String subCategoryAText = "TESTA";
    private static String subCategoryBText = "TESTB";

    private TaskSubcategories taskSubCategories;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskSubCategories = Mockito.spy(TaskSubcategories.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        Mockito.doNothing().when(taskSubCategories).onCreate(any(Bundle.class));
        Mockito.doNothing().when(taskSubCategories).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskSubCategories).finish();
        Mockito.doNothing().when(taskSubCategories).startActivity(any(Intent.class));

        // Mock task builder.
        Utility.currentTaskBuilder = PowerMockito.mock(ImmutableTask.Builder.class);

        // Mock buttons.
        taskSubCategories.subcategoryA = PowerMockito.mock(Button.class);
        when(taskSubCategories.subcategoryA.getText()).thenReturn(subCategoryAText);
        taskSubCategories.subcategoryB = PowerMockito.mock(Button.class);
        when(taskSubCategories.subcategoryB.getText()).thenReturn(subCategoryBText);
    }

    @After
    public void tearDown() throws Exception {
        Utility.currentTaskBuilder = null;
    }

    @Test
    public void taskDetailsA() {
        verify(Utility.currentTaskBuilder, times(0))
                .subCategory(subCategoryAText);
        taskSubCategories.taskDetailsA();
        verify(Utility.currentTaskBuilder, times(1))
                .subCategory(subCategoryAText);
    }

    @Test
    public void taskDetailsB() {
        verify(Utility.currentTaskBuilder, times(0))
                .subCategory(subCategoryBText);
        taskSubCategories.taskDetailsB();
        verify(Utility.currentTaskBuilder, times(1))
                .subCategory(subCategoryBText);
    }
}