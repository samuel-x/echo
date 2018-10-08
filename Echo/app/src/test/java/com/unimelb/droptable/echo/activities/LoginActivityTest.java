package com.unimelb.droptable.echo.activities;

import android.os.Bundle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class LoginActivityTest {

    private String validUsername = "MyUsername";
    private String validPhoneNumber = "0412356789";
    private String invalidUsername = "n";
    private String invalidPhoneNumber = "0";

    private LoginActivity login;

    @Before
    public void setUp() throws Exception {
        // Assign values
        login = new LoginActivity();
    }

    @Test
    public void testUsername() throws Exception {
        assertEquals(login.isUsernameValid(validUsername), true);
        assertEquals(login.isUsernameValid(invalidUsername), false);
    }

    @Test
    public void testPhoneNumber() throws Exception {
        assertEquals(login.isPhoneNumberValid(validPhoneNumber), true);
        assertEquals(login.isPhoneNumberValid(invalidPhoneNumber), false);
    }

}