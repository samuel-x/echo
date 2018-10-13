package com.unimelb.droptable.echo.clientTaskManagement;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimelb.droptable.echo.ClientInfo;

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
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest(FirebaseAdapter.class)
@SuppressStaticInitializationFor("com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter")
public class FirebaseAdapterTest {

    private static final String TEST_TASK_KEY = "TEST_TASK_KEY";

    private DatabaseReference masterMock;
    private DatabaseReference tasksMock;
    private DatabaseReference messagesMock;
    private DatabaseReference usersMock;

    @Before
    public void setUp() throws Exception {
        // Mock FirebaseAdapter.
        PowerMockito.mockStatic(FirebaseAdapter.class);

        // Create field mocks.
        masterMock = Mockito.mock(DatabaseReference.class);
        usersMock = Mockito.mock(DatabaseReference.class);
        tasksMock = Mockito.mock(DatabaseReference.class);
        messagesMock = Mockito.mock(DatabaseReference.class);
        FirebaseAdapter.database = Mockito.mock(FirebaseDatabase.class);
        FirebaseAdapter.masterDbReference = masterMock;
        FirebaseAdapter.tasksDbReference = tasksMock;
        FirebaseAdapter.messagesDbReference = messagesMock;
        FirebaseAdapter.usersDbReference = usersMock;

        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.pushTask(any());
        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.updateTask(any(), any());
        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.updateTaskStatus(any(), any());
        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.updateTaskAssistant(any(), any());
        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.getCurrentTaskID();
        PowerMockito.doCallRealMethod().when(FirebaseAdapter.class);
        FirebaseAdapter.getUser(any());
    }

    /**
     * Checks that a task is pushed to the tasks db, and that a task is added to a user in user db.
     */
    @Test
    public void pushTask() {
        // Prepare test.
        when(FirebaseAdapter.tasksDbReference.push()).thenReturn(tasksMock);
        Mockito.doReturn(null).when(tasksMock).setValue(any());
        when(tasksMock.child(any())).thenReturn(tasksMock);
        ClientInfo.setUsername("TEST_USERNAME");
        when(usersMock.child(any())).thenReturn(usersMock);
        when(tasksMock.getKey()).thenReturn(TEST_TASK_KEY);


        // Verify prior.
        verify(tasksMock, times(0)).push();
        verify(usersMock, times(0)).setValue(TEST_TASK_KEY);

        // Execute.
        FirebaseAdapter.pushTask(null);

        // Verify post.
        verify(tasksMock, times(1)).push();
        verify(usersMock, times(1)).setValue(TEST_TASK_KEY);
    }

    /**
     * Checks that a task is retrieved and updated.
     */
    @Test
    public void updateTask() {
        final String TEST_ID = "TEST_ID";

        // Prepare test.
        when(FirebaseAdapter.tasksDbReference.child(TEST_ID)).thenReturn(tasksMock);
        Mockito.doReturn(null).when(tasksMock).setValue(any());

        // Verify prior.
        verify(FirebaseAdapter.tasksDbReference, times(0)).child(TEST_ID);
        verify(tasksMock, times(0)).setValue(any());

        // Execute.
        FirebaseAdapter.updateTask(null, TEST_ID);

        // Verify post.
        verify(FirebaseAdapter.tasksDbReference, times(1)).child(TEST_ID);
        verify(tasksMock, times(1)).setValue(any());
    }

    /**
     * Checks that a task status is retrieved and updated.
     */
    @Test
    public void updateTaskStatus() {
        final String TEST_ID = "TEST_ID";
        final String TEST_STATUS = "TEST_STATUS";

        // Prepare test.
        when(FirebaseAdapter.tasksDbReference.child(TEST_ID)).thenReturn(tasksMock);
        when(tasksMock.child("status")).thenReturn(tasksMock);
        Mockito.doReturn(null).when(tasksMock).setValue(any());

        // Verify prior.
        verify(FirebaseAdapter.tasksDbReference, times(0)).child(TEST_ID);
        verify(tasksMock, times(0)).setValue(TEST_STATUS);

        // Execute.
        FirebaseAdapter.updateTaskStatus(TEST_STATUS, TEST_ID);

        // Verify post.
        verify(FirebaseAdapter.tasksDbReference, times(1)).child(TEST_ID);
        verify(tasksMock, times(1)).setValue(TEST_STATUS);
    }

    /**
     * Checks that a task status is retrieved and updated.
     */
    @Test
    public void updateTaskAssistant() {
        final String TEST_ID = "TEST_ID";
        final String TEST_ASSISTANT = "TEST_ASSISTANT";

        // Prepare test.
        when(FirebaseAdapter.tasksDbReference.child(TEST_ID)).thenReturn(tasksMock);
        when(tasksMock.child("assistant")).thenReturn(tasksMock);
        Mockito.doReturn(null).when(tasksMock).setValue(any());

        // Verify prior.
        verify(FirebaseAdapter.tasksDbReference, times(0)).child(TEST_ID);
        verify(tasksMock, times(0)).setValue(TEST_ASSISTANT);

        // Execute.
        FirebaseAdapter.updateTaskAssistant(TEST_ASSISTANT, TEST_ID);

        // Verify post.
        verify(FirebaseAdapter.tasksDbReference, times(1)).child(TEST_ID);
        verify(tasksMock, times(1)).setValue(TEST_ASSISTANT);
    }

    /**
     * Checks that a task status is retrieved and updated.
     */
    @Test
    public void getCurrentTaskID() {
        final String TEST_VALUE = "TEST_VALUE";
        final String TEST_USERNAME = "TEST_USERNAME";

        // Prepare test.
        DataSnapshot snapshotMock = mock(DataSnapshot.class);
        when(snapshotMock.getValue(String.class)).thenReturn(TEST_VALUE);
        ClientInfo.setUsername(TEST_USERNAME);

        // Verify prior.
                verify(snapshotMock, times(0))
                .child(TEST_USERNAME);
        verify(snapshotMock, times(0))
                .getValue(String.class);

        // Execute with null current data.
        String result = FirebaseAdapter.getCurrentTaskID();

        // Verify post 1.
        verify(snapshotMock, times(0))
                .child(TEST_USERNAME);
        verify(snapshotMock, times(0))
                .getValue(String.class);
        assertNull(result);

        // Initialize and check the currentData.
        FirebaseAdapter.currentData = snapshotMock;
        when(FirebaseAdapter.currentData.child(any())).thenReturn(snapshotMock);
        verify(FirebaseAdapter.currentData, times(0))
                .child(FirebaseAdapter.USERS_ROOT);

        // Execute with non-null current data.
        result = FirebaseAdapter.getCurrentTaskID();

        // Verify post 2.
        verify(FirebaseAdapter.currentData, times(1))
                .child(FirebaseAdapter.USERS_ROOT);
        verify(snapshotMock, times(1))
                .child(TEST_USERNAME);
        verify(snapshotMock, times(1))
                .getValue(String.class);
        assertEquals(TEST_VALUE, result);
    }

    /**
     * Checks that the specified user is retrieved.
     */
    @Test
    public void getUser() {
        final String TEST_USERNAME = "TEST_USERNAME";

        // Prepare test.
        DataSnapshot snapshotMock = mock(DataSnapshot.class);
        FirebaseAdapter.currentData = snapshotMock;
        when(FirebaseAdapter.currentData.child(FirebaseAdapter.USERS_ROOT)).thenReturn(snapshotMock);
        when(snapshotMock.child(TEST_USERNAME)).thenReturn(null);

        // Verify prior.
        verify(FirebaseAdapter.currentData, times(0))
                .child(FirebaseAdapter.USERS_ROOT);
        verify(snapshotMock, times(0)).child(TEST_USERNAME);

        // Execute.
        FirebaseAdapter.getUser(TEST_USERNAME);

        // Verify post.
        verify(FirebaseAdapter.currentData, times(1))
                .child(FirebaseAdapter.USERS_ROOT);
        verify(snapshotMock, times(1)).child(TEST_USERNAME);
    }

    @After
    public void tearDown() throws Exception {
        ClientInfo.resetClientInfo();
        FirebaseAdapter.currentData = null;
    }
}