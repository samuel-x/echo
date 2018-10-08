package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;

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
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, TaskCategories.class})
public class TaskCategoriesTest {

    private TaskCategories taskCategories;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskCategories = Mockito.spy(TaskCategories.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskCategories).finish();
        Mockito.doNothing().when(taskCategories).startActivity(intentMock);

        // Mock task builder.
        Utility.currentTaskBuilder = PowerMockito.mock(ImmutableTask.Builder.class);
    }

    @After
    public void tearDown() throws Exception {
        // Begin a new taskBuilder object.
        Utility.currentTaskBuilder = null;
    }

    @Test
    public void otherTask() {
        verify(Utility.currentTaskBuilder, times(0)).category("Other");
        taskCategories.otherTask();
        verify(Utility.currentTaskBuilder, times(1)).category("Other");
    }

    @Test
    public void householdTask() {
        verify(Utility.currentTaskBuilder, times(0)).category("Household");
        taskCategories.householdTask();
        verify(Utility.currentTaskBuilder, times(1)).category("Household");
    }

    @Test
    public void deliveryTask() {
        verify(Utility.currentTaskBuilder, times(0)).category("Delivery");
        taskCategories.deliveryTask();
        verify(Utility.currentTaskBuilder, times(1)).category("Delivery");
    }

    @Test
    public void transportTask() {
        verify(Utility.currentTaskBuilder, times(0)).category("Transport");
        taskCategories.transportTask();
        verify(Utility.currentTaskBuilder, times(1)).category("Transport");
    }
}