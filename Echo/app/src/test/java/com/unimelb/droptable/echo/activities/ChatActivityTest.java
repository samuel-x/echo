package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.widget.EditText;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This tests the Chat Activity
 */
@PrepareForTest({ChatActivity.class})
public class ChatActivityTest {

    ChatActivity chatActivity;

    private static String TEST_MESSAGE = "TEST";
    private static int setTextNull = 0;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        chatActivity = Mockito.spy(ChatActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);


        // Define mock behaviors.
        Mockito.doNothing().when(chatActivity).onCreate(any(Bundle.class));
        Mockito.doNothing().when(chatActivity).setContentView(any(int.class));
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(chatActivity).finish();
        Mockito.doNothing().when(chatActivity).startActivity(any(Intent.class));
        Mockito.doNothing().when(chatActivity).sendMessage();

        chatActivity.inputText = PowerMockito.mock(EditText.class);
        Editable mockEdit = PowerMockito.mock(Editable.class);
        when(chatActivity.inputText.getText()).thenReturn(mockEdit);
        when(chatActivity.inputText.getText().toString()).thenReturn(TEST_MESSAGE);
        Mockito.doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                setTextNull++;
                return null;
            };
        }).when(chatActivity.inputText).setText("");
    }

    @After
    public void tearDown() throws Exception {
        setTextNull = 0;
    }

    @Test
    public void testSendMessage() throws Exception {
        // Test sending a message
        when(chatActivity.inputText.getText().toString()).thenReturn(TEST_MESSAGE);
        verify(chatActivity, times(0)).sendMessage();
        assertEquals(setTextNull, 0);
        chatActivity.onMessageSend();
        verify(chatActivity, times(1)).sendMessage();
        assertEquals(setTextNull, 1);
    }

    @Test
    public void testNullMessage() throws Exception {
        when(chatActivity.inputText.getText().toString()).thenReturn("");
        verify(chatActivity, times(0)).sendMessage();
        assertEquals(setTextNull, 0);
        chatActivity.onMessageSend();
        verify(chatActivity, times(0)).sendMessage();
        assertEquals(setTextNull, 0);
    }
}