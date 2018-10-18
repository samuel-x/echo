package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;

import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, LoginActivity.class})
public class LoginActivityTest {

    private static String VALID_USERNAME = "MyUsername";
    private static String VALID_PHONE = "0412356789";
    private static String INVALID_USERNAME = "n";
    private static String INVALID_PHONE = "0";

    private LoginActivity loginActivity;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        loginActivity = Mockito.spy(LoginActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);


        // Define mock behaviors.
        Mockito.doNothing().when(loginActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(loginActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(loginActivity).finish();
        Mockito.doNothing().when(loginActivity).startActivity(any(Intent.class));

        // Mock attemptLogin, to circumvent Firebase
        Mockito.doNothing().when(loginActivity).attemptLogin();
    }

    @Test
    public void testSignInButton() throws Exception {
        verify(loginActivity, times(0)).attemptLogin();
        loginActivity.onSignInClick();
        verify(loginActivity, times(1)).attemptLogin();
    }

    @Test
    public void testValidUsername() {
        assertTrue(loginActivity.isUsernameValid(VALID_USERNAME));
        assertFalse(loginActivity.isUsernameValid(INVALID_USERNAME));
    }

    @Test
    public void testValidPhoneNumber() {
        assertTrue(loginActivity.isPhoneNumberValid(VALID_PHONE));
        assertFalse(loginActivity.isPhoneNumberValid(INVALID_PHONE));
    }

}