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
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, TaskCategories.class})
public class TaskCategoriesTest {

    private TaskCategories taskCategories;
    private Intent intentMock;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskCategories = Mockito.spy(TaskCategories.class);
        intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskCategories).finish();
        Mockito.doNothing().when(taskCategories).startActivity(any(Intent.class));

        // Mock task builder.
        Utility.currentTaskBuilder = PowerMockito.mock(ImmutableTask.Builder.class);
    }

    @After
    public void tearDown() throws Exception {
        // Begin a new taskBuilder object.
        Utility.currentTaskBuilder = null;
    }

    @Test
    public void otherTask() throws Exception {
        verify(Utility.currentTaskBuilder, times(0)).category("Other");
        verify(taskCategories, times(0)).startActivity(intentMock);

        taskCategories.otherTask();

        verify(Utility.currentTaskBuilder, times(1)).category("Other");
        PowerMockito.verifyNew(Intent.class).withArguments(taskCategories, TaskDetails.class);
        verify(taskCategories, times(1)).startActivity(intentMock);
    }

    @Test
    public void householdTask() throws Exception {
        verify(Utility.currentTaskBuilder, times(0)).category("Household");
        verify(taskCategories, times(0)).startActivity(intentMock);

        taskCategories.householdTask();

        verify(Utility.currentTaskBuilder, times(1)).category("Household");
        PowerMockito.verifyNew(Intent.class).withArguments(taskCategories, TaskSubcategories.class);
        verify(taskCategories, times(1)).startActivity(intentMock);
    }

    @Test
    public void deliveryTask() throws Exception {
        verify(Utility.currentTaskBuilder, times(0)).category("Delivery");
        verify(taskCategories, times(0)).startActivity(intentMock);

        taskCategories.deliveryTask();

        verify(Utility.currentTaskBuilder, times(1)).category("Delivery");
        PowerMockito.verifyNew(Intent.class).withArguments(taskCategories, TaskSubcategories.class);
        verify(taskCategories, times(1)).startActivity(intentMock);
    }

    @Test
    public void transportTask() throws Exception {
        verify(Utility.currentTaskBuilder, times(0)).category("Transport");
        verify(taskCategories, times(0)).startActivity(intentMock);

        taskCategories.transportTask();

        verify(Utility.currentTaskBuilder, times(1)).category("Transport");
        PowerMockito.verifyNew(Intent.class).withArguments(taskCategories, TaskSubcategories.class);
        verify(taskCategories, times(1)).startActivity(intentMock);
    }
}