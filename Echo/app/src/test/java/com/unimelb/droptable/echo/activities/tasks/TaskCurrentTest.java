package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.HelperActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.core.classloader.annotations.SuppressStaticInitializationFor;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TaskCurrent.class, TextView.class, String.class, FirebaseAdapter.class,
        ContextCompat.class, HelperActivity.class})
@SuppressStaticInitializationFor("com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter")
public class TaskCurrentTest {

    private TaskCurrent taskCurrent;
    private final static String UNKNOWN = "Unknown";
    private final static int TEST_CHILD_AVATAR = 3;
    private final static int TEST_CHILD_MESSAGE = 2;
    private static int testChildAvatarCounter = 0;
    private static int testChildMessageCounter = 0;
    private final String TEST_USER_NAME = "AS&(8asdg";
    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testStatus = "MyStatus";
    private final String testAp = "MyAp";
    private final String testAssistant = "MyAssistant";
    private final String testLastPhase = "MyLastPhase";
    private final String testNotes = "MyNotes";
    private static final String TEST_PHONE_NUMBER = "0412356789";

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        taskCurrent = PowerMockito.mock(TaskCurrent.class);
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

        // Define text views.
        TextView otherUserName = PowerMockito.mock(TextView.class);
        doNothing().when(otherUserName).setText(any());
        taskCurrent.otherUserName = otherUserName;
        TextView otherUserPhone = PowerMockito.mock(TextView.class);
        doNothing().when(otherUserPhone).setText(any());
        taskCurrent.otherUserPhone = otherUserPhone;
        TextView otherUserRating = PowerMockito.mock(TextView.class);
        doNothing().when(otherUserRating).setText(any());
        taskCurrent.otherUserRating = otherUserRating;

        taskCurrent.avatar = PowerMockito.mock(ConstraintLayout.class);
        when(taskCurrent.avatar.getChildCount()).thenReturn(TEST_CHILD_AVATAR);
        Mockito.doAnswer(invocation -> {
            testChildAvatarCounter++;
            return PowerMockito.mock(View.class);
        }).when(taskCurrent.avatar).getChildAt(any(int.class));

        taskCurrent.searchingMessage = PowerMockito.mock(ConstraintLayout.class);
        when(taskCurrent.searchingMessage.getChildCount()).thenReturn(TEST_CHILD_MESSAGE);
        Mockito.doAnswer(invocation -> {
            testChildMessageCounter++;
            return PowerMockito.mock(View.class);
        }).when(taskCurrent.searchingMessage).getChildAt(any(int.class));
    }

    @After
    public void tearDown() {
        testChildAvatarCounter = 0;
        testChildMessageCounter = 0;
        ClientInfo.resetClientInfo();
    }

    // TODO: Write tests for makeCall() and the MessageButton.

    @Test
    public void testEnableAvatarElement() {
        doCallRealMethod().when(taskCurrent).enableAvatar();

        assertEquals(0, testChildAvatarCounter);
        assertEquals(0, testChildMessageCounter);
        verify(taskCurrent.avatar, times(0)).getChildCount();
        verify(taskCurrent.searchingMessage, times(0)).getChildCount();
        taskCurrent.enableAvatar();
        verify(taskCurrent.avatar, times(4)).getChildCount();
        verify(taskCurrent.searchingMessage, times(3)).getChildCount();
        assertEquals(3, testChildAvatarCounter);
        assertEquals(2, testChildMessageCounter);
    }

    @Test
    public void testEnableElement() {
        doCallRealMethod().when(taskCurrent).enableElement(any());

        View mockView = PowerMockito.mock(View.class);
        verify(mockView, times(0)).setAlpha(1.0f);
        verify(mockView, times(0)).setEnabled(true);
        taskCurrent.enableElement(mockView);
        verify(mockView, times(1)).setAlpha(1.0f);
        verify(mockView, times(1)).setEnabled(true);
    }

    @Test
    public void testDisableElement() {
        doCallRealMethod().when(taskCurrent).disableElement(any(), anyBoolean());

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
        doCallRealMethod().when(taskCurrent).disableAvatar();

        assertEquals(0, testChildAvatarCounter);
        assertEquals(0, testChildMessageCounter);
        verify(taskCurrent.avatar, times(0)).getChildCount();
        verify(taskCurrent.searchingMessage, times(0)).getChildCount();
        taskCurrent.disableAvatar();
        verify(taskCurrent.avatar, times(4)).getChildCount();
        verify(taskCurrent.searchingMessage, times(3)).getChildCount();
        assertEquals(3, testChildAvatarCounter);
        assertEquals(2, testChildMessageCounter);
    }

    @Test
    public void testBind() {
        doCallRealMethod().when(taskCurrent).bind(any());

        // Create fake task.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .notes(testNotes)
                .lastPhase(testLastPhase)
                .build();

        // Define mock behavior.
        doNothing().when(taskCurrent).setTitle(task.getTitle());
        doNothing().when(taskCurrent).setAddress(task.getAddress());
        doNothing().when(taskCurrent).setNotes(task.getNotes());

        // Verify prior.
 //       verify(taskCurrent, times(0)).setTitle(task.getTitle()); // TODO: Fix
        verify(taskCurrent, times(0)).setAddress(task.getAddress());
        verify(taskCurrent, times(0)).setNotes(task.getNotes());
        verify(taskCurrent, times(0)).updateAssistant(any());

        // Execute 1.
        taskCurrent.bind(task);

        // Verify post 1.
 //        verify(taskCurrent, times(1)).setTitle(task.getTitle()); // TODO: Fix
        verify(taskCurrent, times(1)).setAddress(task.getAddress());
        verify(taskCurrent, times(1)).setNotes(task.getNotes());
        verify(taskCurrent, times(0)).updateAssistant(task.getAssistant());

        // Add assistant.
        task = ImmutableTask.builder().from(task).assistant(testAssistant).build();
        doNothing().when(taskCurrent).updateAssistant(task.getAssistant());

        // Execute 2.
        taskCurrent.bind(task);

        // Verify post 2.
//        verify(taskCurrent, times(2)).setTitle(task.getTitle()); // TODO: Fix
        verify(taskCurrent, times(2)).setAddress(task.getAddress());
        verify(taskCurrent, times(2)).setNotes(task.getNotes());
        verify(taskCurrent, times(1)).updateAssistant(task.getAssistant());
    }

    @Test
    public void testUpdateAssistant() {
        final String TEST_ASSISTANT_ID = "TEST_ASSISTANT_ID";

        doCallRealMethod().when(taskCurrent).updateAssistant(any());

        // Make fake task for ClientInfo.
        ImmutableTask task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .status(testStatus)
                .ap(testAp)
                .lastPhase(testLastPhase)
                .notes(testNotes)
                .build();
        ClientInfo.setTask(task);

        // Mock behavior.
        when(taskCurrent.getString(anyInt())).thenReturn("0");
        ClientInfo.setIsAssistant(false);
        DataSnapshot snapshotMock = mock(DataSnapshot.class);
        FirebaseAdapter.currentData = snapshotMock;
        when(snapshotMock.child(any())).thenReturn(snapshotMock);
        when(snapshotMock.hasChild(any())).thenReturn(true);
        when(snapshotMock.getValue(String.class)).thenReturn(TEST_PHONE_NUMBER);
        when(snapshotMock.getValue(Float.class)).thenReturn(0f);
        PowerMockito.mockStatic(FirebaseAdapter.class);
        PowerMockito.doReturn(snapshotMock).when(FirebaseAdapter.class);
        FirebaseAdapter.getUser(any());

        // Verify prior.
        verify(taskCurrent.otherUserName, times(0)).setText(any());
        verify(taskCurrent.otherUserPhone, times(0)).setText(any());
        verify(taskCurrent.otherUserRating, times(0)).setText(any());

        // Execute 1.
        taskCurrent.updateAssistant(TEST_ASSISTANT_ID);

        // Verify post 1.
        verify(taskCurrent.otherUserName, times(1)).setText(any());
        verify(taskCurrent.otherUserPhone, times(1)).setText(any());
        verify(taskCurrent.otherUserRating, times(1)).setText(any());

        // Make assistant.
        ClientInfo.setIsAssistant(true);

        // Execute 2.
        taskCurrent.updateAssistant(TEST_ASSISTANT_ID);

        // Verify post 2.
        verify(taskCurrent.otherUserName, times(2)).setText(any());
        verify(taskCurrent.otherUserPhone, times(2)).setText(any());
        verify(taskCurrent.otherUserRating, times(1)).setText(any());
    }

    @Test
    public void testResetAssistant() {
        doCallRealMethod().when(taskCurrent).resetAssistant();

        // Prepare test.
        ClientInfo.setIsAssistant(false);

        // Verify prior.
        verify(taskCurrent.otherUserName, times(0)).setText(any());
        verify(taskCurrent.otherUserPhone, times(0)).setText(any());

        // Execute.
        taskCurrent.resetAssistant();

        // Verify post.
        verify(taskCurrent.otherUserName, times(1)).setText(any());
        verify(taskCurrent.otherUserPhone, times(1)).setText(any());
    }

    @Test
    public void testOnCallButtonClick() {
        doCallRealMethod().when(taskCurrent).onCallButtonClick();

        // Get the "no phone" resource.
        final String NO_PHONE_NUMBER = "(No phone number)";

        // Prepare execution for no phone number.
        when(taskCurrent.otherUserPhone.getText()).thenReturn(NO_PHONE_NUMBER);
        when(taskCurrent.getString(R.string.empty_phone_number)).thenReturn(NO_PHONE_NUMBER);
        ClientInfo.setIsAssistant(false);
        doNothing().when(taskCurrent).makeCall();

        // Verify prior.
        verify(taskCurrent, times(0)).makeCall();

        // Execute 1.
        taskCurrent.onCallButtonClick();

        // Verify post 1.
        verify(taskCurrent, times(0)).makeCall();

        // Prepare for execution with phone number and permission.
        when(taskCurrent.otherUserPhone.getText()).thenReturn(TEST_PHONE_NUMBER);
        ClientInfo.setIsAssistant(true);
        PowerMockito.mockStatic(ContextCompat.class);
        PowerMockito.doReturn(PackageManager.PERMISSION_GRANTED).when(ContextCompat.class);
        ContextCompat.checkSelfPermission(any(), eq(android.Manifest.permission.CALL_PHONE));

        // Execute 2.
        taskCurrent.onCallButtonClick();

        // Verify post 2.
        verify(taskCurrent, times(1)).makeCall();
    }

    @Test
    public void testMakeCall() throws Exception {
        doCallRealMethod().when(taskCurrent).makeCall();

        // Mock behavior.
        Intent intentMock = Mockito.mock(Intent.class);
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(taskCurrent.otherUserPhone.getText()).thenReturn(TEST_PHONE_NUMBER);
        when(intentMock.setData(any())).thenReturn(intentMock);

        // Verify prior.
        PowerMockito.verifyNew(Intent.class, times(0)).withArguments(Intent.ACTION_CALL);
        verify(taskCurrent, times(0)).startActivity(intentMock);

        // Execute.
        taskCurrent.makeCall();

        // Verify post.
        PowerMockito.verifyNew(Intent.class, times(1)).withArguments(Intent.ACTION_CALL);
        verify(taskCurrent, times(1)).startActivity(intentMock);
    }

    @Test
    public void testOnHelperPress() throws Exception {
        doCallRealMethod().when(taskCurrent).onHelperPress();

        // Mock behavior.
        Intent intentMock = Mockito.mock(Intent.class);
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        PowerMockito.mockStatic(HelperActivity.class);
        PowerMockito.doNothing().when(HelperActivity.class);
        HelperActivity.setCurrentHelperText(any());

        // Verify prior.
        PowerMockito.verifyNew(Intent.class, times(0)).withArguments(any(), eq(HelperActivity.class));
        verify(taskCurrent, times(0)).startActivity(intentMock);

        // Execute.
        taskCurrent.onHelperPress();

        // Verify post.
        PowerMockito.verifyNew(Intent.class, times(1)).withArguments(any(), eq(HelperActivity.class));
        verify(taskCurrent, times(1)).startActivity(intentMock);
    }
}