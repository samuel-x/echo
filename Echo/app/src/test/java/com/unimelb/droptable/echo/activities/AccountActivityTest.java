package com.unimelb.droptable.echo.activities;

import android.accounts.Account;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.RatingBar;
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
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.net.HttpURLConnection;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Account.class, TextView.class, RatingBar.class, ClientInfo.class})
public class AccountActivityTest {

    private AccountActivity accountActivity;
    private Intent intentMock;

    private static final String TEST_USER = "TEST_USER";
    private static final String TEST_PHONENUMBER = "TEST_ID";
    private static final float TEST_RATING = 5.0f;
    private static final Boolean IS_ASSITANT = true;
    private static final Boolean IS_AP = false;

    @Before
    public void setUp() throws Exception {
        accountActivity = Mockito.spy(AccountActivity.class);
        accountActivity.ratingBar = Mockito.mock(RatingBar.class);
        accountActivity.isAssistantText = Mockito.mock(TextView.class);
        accountActivity.phone = Mockito.mock(TextView.class);
        accountActivity.username = Mockito.mock(TextView.class);
        intentMock = Mockito.mock(Intent.class);

        //Mock ClientInfo
        PowerMockito.mockStatic(ClientInfo.class);
        PowerMockito.doReturn(TEST_USER).when(ClientInfo.class);
        ClientInfo.getUsername();
        PowerMockito.doReturn(TEST_PHONENUMBER).when(ClientInfo.class);
        ClientInfo.getPhoneNumber();
        PowerMockito.doReturn(TEST_RATING).when(ClientInfo.class);
        ClientInfo.getRating();

        //Mock Behaviours
        Mockito.doNothing().when(accountActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(accountActivity).setContentView(anyInt());
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
    }

    @After
    public void tearDown() throws Exception {
        ClientInfo.resetClientInfo();
    }

    /** Tests if the UI is correctly updated as an Assistant */
    @Test
    public void testUpdateUIasAssistant(){
        //Make the isAssistant variable true
        PowerMockito.doReturn(IS_ASSITANT).when(ClientInfo.class);
        ClientInfo.isAssistant();

        // Verify prior.
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getPhoneNumber();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.isAssistant();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getRating();
        verify(accountActivity.username, times(0)).setText(TEST_USER);
        verify(accountActivity.phone,times(0)).setText(TEST_PHONENUMBER);
        verify(accountActivity.ratingBar, times(0)).setRating(TEST_RATING);
        verify(accountActivity.isAssistantText, times(0)).setEnabled(true);
        verify(accountActivity.ratingBar,times(0)).setEnabled(true);

        //Execute
        accountActivity.updateUI();

        //Verify post
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getPhoneNumber();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.isAssistant();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getRating();
        verify(accountActivity.username, times(1)).setText(TEST_USER);
        verify(accountActivity.phone,times(1)).setText(TEST_PHONENUMBER);
        verify(accountActivity.ratingBar, times(1)).setRating(TEST_RATING);
        verify(accountActivity.isAssistantText, times(1)).setEnabled(true);
        verify(accountActivity.ratingBar,times(1)).setEnabled(true);
    }

    /** Tests if the UI is correctly updated as an AP */
    @Test
    public void testUpdateUIasAP(){
        //Make the isAssistant variable true
        PowerMockito.doReturn(IS_AP).when(ClientInfo.class);
        ClientInfo.isAssistant();

        //Verify Prior
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.getPhoneNumber();
        PowerMockito.verifyStatic(ClientInfo.class, times(0));
        ClientInfo.isAssistant();
        verify(accountActivity.username, times(0)).setText(TEST_USER);
        verify(accountActivity.phone,times(0)).setText(TEST_PHONENUMBER);
        verify(accountActivity.isAssistantText, times(0)).setEnabled(false);
        verify(accountActivity.isAssistantText, times(0)).setAlpha(0.0f);
        verify(accountActivity.ratingBar, times(0)).setEnabled(false);
        verify(accountActivity.ratingBar,times(0)).setAlpha(0.0f);

        //Execute
        accountActivity.updateUI();

        //Verify Post
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getUsername();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.getPhoneNumber();
        PowerMockito.verifyStatic(ClientInfo.class, times(1));
        ClientInfo.isAssistant();
        verify(accountActivity.username, times(1)).setText(TEST_USER);
        verify(accountActivity.phone,times(1)).setText(TEST_PHONENUMBER);
        verify(accountActivity.isAssistantText, times(1)).setEnabled(false);
        verify(accountActivity.isAssistantText, times(1)).setAlpha(0.0f);
        verify(accountActivity.ratingBar, times(1)).setEnabled(false);
        verify(accountActivity.ratingBar,times(1)).setAlpha(0.0f);
    }
}
