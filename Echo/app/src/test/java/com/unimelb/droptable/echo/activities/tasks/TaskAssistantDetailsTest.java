package com.unimelb.droptable.echo.activities.tasks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ChatActivity;
import com.unimelb.droptable.echo.activities.RatingActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static android.provider.Settings.Global.getString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.HttpURLConnection;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskAssistantDetails.class, TextView.class, String.class,
                                     FirebaseAdapter.class, ClientInfo.class})
@SuppressStaticInitializationFor("com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter")
public class TaskAssistantDetailsTest {

    private TaskAssistantDetails taskAssistantDetails;
    private Intent intentMock;
    private ImmutableTask task;

    private static final String TEST_TITLE = "Test TEST_TITLE";
    private static final String TEST_ADDRESS = "TEST_ADDRESS";
    private static final String TEST_NOTES = "TEST_NOTES";
    private static final String TEST_STATUS = "TEST_STATUS";
    private static final String TEST_AP = "TEST_AP";
    private static final String TEST_USER = "TEST_USER";
    private static final String TEST_ID = "TEST_ID";
    private static final String TEST_LAST_PHASE = "TEST_LAST_PHASE";

    @Before
    public void setUp() throws Exception {
        taskAssistantDetails = Mockito.spy(TaskAssistantDetails.class);
        intentMock = Mockito.mock(Intent.class);

        //Mock ClientInfo
        PowerMockito.mockStatic(ClientInfo.class);
        PowerMockito.doReturn(TEST_USER).when(ClientInfo.class);
        ClientInfo.getUsername();

        //Mock Firebase
        PowerMockito.mockStatic(FirebaseAdapter.class);

        //Mock Behaviours
        Mockito.doNothing().when(taskAssistantDetails).onCreate(any(Bundle.class));
        Mockito.doNothing().when(taskAssistantDetails).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskAssistantDetails).finish();
        Mockito.doNothing().when(taskAssistantDetails).startActivity(any());

        //Create the test task
        task = ImmutableTask.builder()
                .title(TEST_TITLE)
                .address(TEST_ADDRESS)
                .notes(TEST_NOTES)
                .status(TEST_STATUS)
                .ap(TEST_AP)
                .id(TEST_ID)
                .lastPhase(TEST_LAST_PHASE)
                .build();
        taskAssistantDetails.task = task;
    }

    @Test
    public void onAccept(){
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.setTask(task);
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(FirebaseAdapter.class,times(0));
        FirebaseAdapter.assignTask(TEST_USER, TEST_ID);
        verify(taskAssistantDetails,times(0)).startActivity(any());

        // Execute
        taskAssistantDetails.onAccept();

        // Verify that static methods were called.
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.setTask(task);
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(FirebaseAdapter.class, times(1));
        FirebaseAdapter.assignTask(TEST_USER, TEST_ID);
        verify(taskAssistantDetails,times(1)).startActivity(any());
    }
}
