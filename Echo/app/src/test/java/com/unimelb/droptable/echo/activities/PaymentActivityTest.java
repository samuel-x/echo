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
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({PaymentActivity.class})
public class PaymentActivityTest {

    PaymentActivity paymentActivity;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        paymentActivity = Mockito.spy(PaymentActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        Mockito.doNothing().when(paymentActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(paymentActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(paymentActivity).finish();
        Mockito.doNothing().when(paymentActivity).startActivity(any(Intent.class));

    }

    @Test
    public void testContinue() throws Exception {
        // Verify activity change.
        paymentActivity.onSubmit();
        PowerMockito.verifyNew(Intent.class).withArguments(paymentActivity, RatingActivity.class);
    }
}