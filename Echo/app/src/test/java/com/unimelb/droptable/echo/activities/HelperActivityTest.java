package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@PrepareForTest({HelperActivity.class})
public class HelperActivityTest {

    HelperActivity helperActivity;

    private static String TEST_MESSAGE = "TEST";

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        helperActivity = Mockito.spy(HelperActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);


        // Define mock behaviors.
        Mockito.doNothing().when(helperActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(helperActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(helperActivity).finish();
        Mockito.doNothing().when(helperActivity).startActivity(any(Intent.class));

    }

    @After
    public void tearDown() throws Exception {
        HelperActivity.setCurrentHelperText(null);
    }

    @Test
    public void testSetCurrentHelperText() {
        HelperActivity.setCurrentHelperText(TEST_MESSAGE);
        assertEquals(HelperActivity.currentHelperText, TEST_MESSAGE);
    }

    @Test
    public void testOnOkButton() {
        verify(helperActivity, times(0)).finish();
        helperActivity.onOkPress();
        verify(helperActivity, times(1)).finish();
    }
}