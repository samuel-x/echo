package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RatingBar;

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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PaymentActivity.class, ClientInfo.class, FirebaseAdapter.class})
@SuppressStaticInitializationFor("com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter")
public class PaymentActivityTest {

    private PaymentActivity paymentActivity;
    private ImmutableTask task;

    private static final String TEST_TITLE = "Test TEST_TITLE";
    private static final String TEST_ADDRESS = "TEST_ADDRESS";
    private static final String TEST_NOTES = "TEST_NOTES";
    private static final String TEST_STATUS = "TEST_STATUS";
    private static final String TEST_AP = "TEST_AP";
    private static final String TEST_USER = "TEST_USER";
    private static final String TEST_ID = "TEST_ID";
    private static final String TEST_LAST_PHASE = "TEST_LAST_PHASE";
    private static float TEST_RATING = 5.0f;

    @Before
    public void setUp() throws Exception {
        //Create the test task
        task = ImmutableTask.builder()
                .title(TEST_TITLE)
                .address(TEST_ADDRESS)
                .notes(TEST_NOTES)
                .status(TEST_STATUS)
                .ap(TEST_AP)
                .id(TEST_ID)
                .assistant(TEST_USER)
                .lastPhase(TEST_LAST_PHASE)
                .build();

        // Create mocks.
        paymentActivity = Mockito.spy(PaymentActivity.class);
        paymentActivity.ratingBar = Mockito.mock(RatingBar.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Mock ClientInfo and FirebaseAdapter
        PowerMockito.mockStatic(ClientInfo.class);
        PowerMockito.mockStatic(FirebaseAdapter.class);
        PowerMockito.doNothing().when(ClientInfo.class);
        ClientInfo.updateTask();
        PowerMockito.doReturn(task).when(ClientInfo.class);
        ClientInfo.getTask();

        // Define mock behaviors.
        Mockito.doNothing().when(paymentActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(paymentActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(paymentActivity).finish();
        Mockito.doNothing().when(paymentActivity).startActivity(any(Intent.class));
        when(paymentActivity.ratingBar.getRating()).thenReturn(TEST_RATING);
    }

    @Test
    public void testContinue() throws Exception {
        // Verify prior.
        verify(paymentActivity, times(0)).completeTask();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.updateTask();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getTask();
        PowerMockito.verifyStatic(FirebaseAdapter.class,times(0));
        FirebaseAdapter.updateUserRating(TEST_USER, TEST_RATING);
        PowerMockito.verifyStatic(FirebaseAdapter.class,times(0));
        FirebaseAdapter.completeTask(task);
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.setTask(null);
        verify(paymentActivity,times(0)).goToMap();
        verify(paymentActivity,times(0)).finish();

        // Execute.
        paymentActivity.onSubmit();

        // Verify post.
        verify(paymentActivity, times(1)).completeTask();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.updateTask();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getTask();
        PowerMockito.verifyStatic(FirebaseAdapter.class,times(1));
        FirebaseAdapter.updateUserRating(TEST_USER, TEST_RATING);
        PowerMockito.verifyStatic(FirebaseAdapter.class,times(1));
        FirebaseAdapter.completeTask(task);
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.setTask(null);
        verify(paymentActivity,times(1)).goToMap();
        verify(paymentActivity,times(1)).finish();

    }
}