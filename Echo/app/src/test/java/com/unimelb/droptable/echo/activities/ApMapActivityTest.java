package com.unimelb.droptable.echo.activities;

import android.content.Intent;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.activities.taskCreation.TaskCategories;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

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
@PrepareForTest({ImmutableTask.Builder.class, ApMapActivity.class})
public class ApMapActivityTest {

    private ApMapActivity apMapActivity;

    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testNotes = "MyNotes";
    private final String ap = "AP";
    private final String testStatus = "MyStatus";
    private final String assistant = "assistant";
    private final String id = "ID";
    private final String lastPhase = "lastPhase";

    private ImmutableTask task;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        apMapActivity = Mockito.spy(ApMapActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(apMapActivity).finish();
        Mockito.doNothing().when(apMapActivity).startActivity(any(Intent.class));

        task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .notes(testNotes)
                .status(testStatus)
                .ap(ap)
                .assistant(assistant)
                .id(id)
                .lastPhase(lastPhase)
                .build();
    }

    @Test
    public void testTaskButtonNoTask() throws Exception {
        // Verify activity change without task.
        verify(apMapActivity, times(0)).onTaskPress();
        apMapActivity.onTaskPress();
        verify(apMapActivity, times(1)).onTaskPress();
        PowerMockito.verifyNew(Intent.class).withArguments(apMapActivity, TaskCategories.class);
    }

    @Test
    public void testTaskButtonWithTask() throws Exception {
        // Verify activity change with task.
        ClientInfo.setTask(task);
        verify(apMapActivity, times(0)).onTaskPress();
        apMapActivity.onTaskPress();
        verify(apMapActivity, times(1)).onTaskPress();
        PowerMockito.verifyNew(Intent.class).withArguments(apMapActivity, TaskCurrent.class);
    }

    @After
    public void tearDown() {
        apMapActivity = null;
        ClientInfo.setTask(null);
        ClientInfo.setUsername(null);
    }
}