package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ChatActivity;
import com.unimelb.droptable.echo.activities.RatingActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static android.provider.Settings.Global.getString;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskCurrent.class, TextView.class, String.class})
public class TaskCurrentTest {

    private TaskCurrent taskCurrent;
    private final static String UNKNOWN = "Unknown";
    private final static int TEST_CHILD_AVATAR = 3;
    private final static int TEST_CHILD_MESSAGE = 2;
    private static int testChildAvatarCounter = 0;
    private static int testChildMessageCounter = 0;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskCurrent = Mockito.spy(TaskCurrent.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        Mockito.doNothing().when(taskCurrent).onCreate(any(Bundle.class));
        Mockito.doNothing().when(taskCurrent).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(taskCurrent).finish();
        Mockito.doNothing().when(taskCurrent).startActivity(any(Intent.class));
        Mockito.doNothing().when(taskCurrent).makeCall();
        Mockito.doNothing().when(taskCurrent).resetAssistant();
        Mockito.doNothing().when(taskCurrent).updateAssistant(any());

        taskCurrent.avatar = PowerMockito.mock(ConstraintLayout.class);
        when(taskCurrent.avatar.getChildCount()).thenReturn(TEST_CHILD_AVATAR);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                testChildAvatarCounter++;
                return PowerMockito.mock(View.class);
            };
        }).when(taskCurrent.avatar).getChildAt(any(int.class));

        taskCurrent.searchingMessage = PowerMockito.mock(ConstraintLayout.class);
        when(taskCurrent.searchingMessage.getChildCount()).thenReturn(TEST_CHILD_MESSAGE);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                testChildMessageCounter++;
                return PowerMockito.mock(View.class);
            };
        }).when(taskCurrent.searchingMessage).getChildAt(any(int.class));
    }

    @After
    public void tearDown() throws Exception {
        testChildAvatarCounter = 0;
        testChildMessageCounter = 0;
    }

    // TODO: Write tests for makeCall() and the MessageButton.

    @Test
    public void testEnableAvatarElement() {
        assertEquals(testChildAvatarCounter, 0);
        assertEquals(testChildMessageCounter, 0);
        verify(taskCurrent.avatar, times(0)).getChildCount();
        verify(taskCurrent.searchingMessage, times(0)).getChildCount();
        taskCurrent.enableAvatar();
        verify(taskCurrent.avatar, times(4)).getChildCount();
        verify(taskCurrent.searchingMessage, times(3)).getChildCount();
        assertEquals(testChildAvatarCounter, 3);
        assertEquals(testChildMessageCounter, 2);
    }

    @Test
    public void testEnableElement() {
        View mockView = PowerMockito.mock(View.class);
        verify(mockView, times(0)).setAlpha(1.0f);
        verify(mockView, times(0)).setEnabled(true);
        taskCurrent.enableElement(mockView);
        verify(mockView, times(1)).setAlpha(1.0f);
        verify(mockView, times(1)).setEnabled(true);
    }

    @Test
    public void testDisableElement() {
        View mockView = PowerMockito.mock(View.class);
        verify(mockView, times(0)).setAlpha(0.0f);
        verify(mockView, times(0)).setAlpha(0.06f);
        verify(mockView, times(0)).setEnabled(false);
        taskCurrent.disableElement(mockView, true);
        verify(mockView, times(1)).setAlpha(0.0f);
        verify(mockView, times(0)).setAlpha(0.06f);
        verify(mockView, times(1)).setEnabled(false);
        taskCurrent.disableElement(mockView, false);
        verify(mockView, times(1)).setAlpha(0.0f);
        verify(mockView, times(1)).setAlpha(0.06f);
        verify(mockView, times(2)).setEnabled(false);
    }

    @Test
    public void testDisableAvatarElement() {
        assertEquals(testChildAvatarCounter, 0);
        assertEquals(testChildMessageCounter, 0);
        verify(taskCurrent.avatar, times(0)).getChildCount();
        verify(taskCurrent.searchingMessage, times(0)).getChildCount();
        taskCurrent.disableAvatar();
        verify(taskCurrent.avatar, times(4)).getChildCount();
        verify(taskCurrent.searchingMessage, times(3)).getChildCount();
        assertEquals(testChildAvatarCounter, 3);
        assertEquals(testChildMessageCounter, 2);
    }
}