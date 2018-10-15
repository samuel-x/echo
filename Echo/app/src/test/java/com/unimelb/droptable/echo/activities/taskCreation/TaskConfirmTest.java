package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.HttpURLConnection;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ClientInfo.class, TaskConfirm.class, FirebaseAdapter.class})
@SuppressStaticInitializationFor("com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter")
public class TaskConfirmTest {

    private static final String TEST_TITLE = "Test TEST_TITLE";
    private static final String TEST_ADDRESS = "TEST_ADDRESS";
    private static final String TEST_NOTES = "TEST_NOTES";
    private static final String TEST_STATUS = "TEST_STATUS";
    private static final String TEST_AP = "TEST_AP";
    private static final String TEST_LAST_PHASE = "TEST_LAST_PHASE";

    private TaskConfirm taskConfirm;
    private ImmutableTask task;
    private Intent intentMock;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskConfirm = Mockito.spy(TaskConfirm.class);
        intentMock = Mockito.mock(Intent.class);

        // Mock ClientInfo.
        PowerMockito.mockStatic(ClientInfo.class);

        // Mock FirebaseAdapter.
        PowerMockito.mockStatic(FirebaseAdapter.class);
        PowerMockito.doReturn(HttpURLConnection.HTTP_OK).when(FirebaseAdapter.class);
        FirebaseAdapter.pushTask(task);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskConfirm).finish();
        Mockito.doNothing().when(taskConfirm).startActivity(any());

        // Create a test task.
        task = ImmutableTask.builder()
                .title(TEST_TITLE)
                .address(TEST_ADDRESS)
                .notes(TEST_NOTES)
                .status(TEST_STATUS)
                .ap(TEST_AP)
                .lastPhase(TEST_LAST_PHASE)
                .build();
        taskConfirm.task = task;
    }

    @Test
    public void confirmSubmit() {
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.setTask(task);
        PowerMockito.verifyStatic(FirebaseAdapter.class, times(0));
        FirebaseAdapter.pushTask(task);
        verify(taskConfirm, times(0)).finish();
        taskConfirm.confirmSubmit();

        // Verify that static methods were called.
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.setTask(task);
        PowerMockito.verifyStatic(FirebaseAdapter.class, times(1));
        FirebaseAdapter.pushTask(task);
        verify(taskConfirm, times(1)).finish();
    }
}