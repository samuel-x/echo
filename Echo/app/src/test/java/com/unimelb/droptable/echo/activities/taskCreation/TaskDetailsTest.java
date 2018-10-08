package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, TaskDetails.class})
public class TaskDetailsTest {

    private static String TEST_TITLE = "TESTA";
    private static String TEST_ADDRESS = "TESTB";
    private static String TEST_TASK_NOTES = "TESTB";

    private TaskDetails taskDetails;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskDetails = Mockito.spy(TaskDetails.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        Mockito.doNothing().when(taskDetails).onCreate(any(Bundle.class));
        Mockito.doNothing().when(taskDetails).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class)
                .withParameterTypes(Context.class, Class.class)
                .withArguments(eq(Context.class), eq(TaskConfirm.class))
                .thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskDetails).finish();
        Mockito.doNothing().when(taskDetails).startActivity(any());

        // Mock task builder.
        Utility.currentTaskBuilder = PowerMockito.mock(ImmutableTask.Builder.class);

        // Mock input text.
        taskDetails.title = PowerMockito.mock(TextView.class);
        when(taskDetails.title.getText()).thenReturn(TEST_TITLE);
        taskDetails.address = PowerMockito.mock(TextView.class);
        when(taskDetails.address.getText()).thenReturn(TEST_ADDRESS);
        taskDetails.taskNotes = PowerMockito.mock(TextView.class);
        when(taskDetails.taskNotes.getText()).thenReturn(TEST_TASK_NOTES);
    }

    @After
    public void tearDown() throws Exception {
        Utility.currentTaskBuilder = null;
    }

    @Test
    public void submitNow() throws Exception {
        // Verify builder was used.
        verify(Utility.currentTaskBuilder, times(0))
                .title(TEST_TITLE);
        verify(Utility.currentTaskBuilder, times(0))
                .address(TEST_ADDRESS);
        verify(Utility.currentTaskBuilder, times(0))
                .notes(TEST_TASK_NOTES);
        taskDetails.submitNow();
        verify(Utility.currentTaskBuilder, times(1))
                .title(TEST_TITLE);
        verify(Utility.currentTaskBuilder, times(1))
                .address(TEST_ADDRESS);
        verify(Utility.currentTaskBuilder, times(1))
                .notes(TEST_TASK_NOTES);

        // Verify activity change.
        PowerMockito.verifyNew(Intent.class).withArguments(taskDetails, TaskConfirm.class);
    }
}