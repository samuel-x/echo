package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;

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
@PrepareForTest({RatingActivity.class})
public class RatingActivityTest {

    RatingActivity ratingActivity;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        ratingActivity = Mockito.spy(RatingActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        Mockito.doNothing().when(ratingActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(ratingActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(ratingActivity).finish();
        Mockito.doNothing().when(ratingActivity).startActivity(any(Intent.class));

        // Do nothing on complete task to circumvent Firebase
        Mockito.doNothing().when(ratingActivity).completeTask();
    }

    @Test
    public void testConfirm() throws Exception {
        // Test that the confirm method is run.
        verify(ratingActivity, times(0)).completeTask();
        ratingActivity.onConfirmRating();
        verify(ratingActivity, times(1)).completeTask();
    }
}