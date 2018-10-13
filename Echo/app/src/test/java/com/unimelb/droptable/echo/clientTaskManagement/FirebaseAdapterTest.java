package com.unimelb.droptable.echo.clientTaskManagement;

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

import static org.mockito.ArgumentMatchers.any;
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
        FirebaseAdapter.pushTask(null);
    }

    /**
     * Checks that a task is pushed to the tasks db, and that a task is added to a user in user db.
     */
    @Test
    public void pushTask() {
        // Define mock behavior.
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

    @After
    public void tearDown() throws Exception {
        ClientInfo.resetClientInfo();
    }
}