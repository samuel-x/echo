package com.unimelb.droptable.echo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.taskCreation.TaskCategories;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantList;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ImmutableTask.Builder.class, AssistantMapActivity.class})
public class AssistantMapActivityTest {

    private AssistantMapActivity assistantMapActivity;

    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testNotes = "MyNotes";
    private final String ap = "AP";
    private final String testStatus = "MyStatus";
    private final String assistant = "assistant";
    private final String id = "ID";

    private ImmutableTask task;

    @Before
    public void setUp() throws Exception {
        // Create mocks.
        assistantMapActivity = Mockito.spy(AssistantMapActivity.class);
        Intent intentMock = Mockito.mock(Intent.class);

        // Define mock behaviors.
        PowerMockito.whenNew(Intent.class).withAnyArguments().thenReturn(intentMock);
        when(intentMock.putExtra(any(String.class), any(String.class))).thenReturn(intentMock);
        Mockito.doNothing().when(assistantMapActivity).finish();
        Mockito.doNothing().when(assistantMapActivity).startActivity(any(Intent.class));
        Mockito.when(assistantMapActivity.createTaskListener()).thenCallRealMethod();

        task = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .notes(testNotes)
                .status(testStatus)
                .ap(ap)
                .assistant(assistant)
                .id(id).build();
    }

    @Test
    public void testTaskButtonNoTask() throws Exception {
        // Verify activity change without task.
        verify(assistantMapActivity, times(0)).onTaskButtonClick();
        assistantMapActivity.onTaskButtonClick();
        verify(assistantMapActivity, times(1)).onTaskButtonClick();
        PowerMockito.verifyNew(Intent.class).withArguments(assistantMapActivity, TaskAssistantList.class);
    }

    @Test
    public void testTaskButtonTask() throws Exception {
        // Verify activity change with task.
        ClientInfo.setTask(task);
        verify(assistantMapActivity, times(0)).onTaskButtonClick();
        assistantMapActivity.onTaskButtonClick();
        verify(assistantMapActivity, times(1)).onTaskButtonClick();
        PowerMockito.verifyNew(Intent.class).withArguments(assistantMapActivity, TaskCurrent.class);
    }

    @Test
    public void createListener() {
        /**
         * Here, mocking the variables inside the listener created alters the hashed result
         * when asserting equals, so the best we can do is just check the type.
         */
        assertThat(assistantMapActivity.createTaskListener(), instanceOf(ChildEventListener.class));
    }

    @After
    public void tearDown() {
        assistantMapActivity = null;
    }
}