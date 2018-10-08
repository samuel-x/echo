package com.unimelb.droptable.echo.clientTaskManagement;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimelb.droptable.echo.ChatMessage;
import com.unimelb.droptable.echo.ClientInfo;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

/**
 * ATTENTION FOR MARKER:
 * This is the test for the Firebase Adapter.
 * Unfortunately, we are not able to test a variety of the functions, as it requires mocking
 * DataSnapshot from the FirebaseDatabase package, which is a final class.
 * This means that any function that utilises this data type within FirebaseAdapter will throw
 * a null pointer exception.
 * In addition, there is no way to test FirebaseDatabase locally, without writing an entire new
 * adapter for a local JSON object, which would defeat the point of writing the test itself if
 * the FirebaseAdapter must be rewritten to do this. Testing this adapter further would also just
 * result in testing the Firebase package itself, which is outside of the recommended usage of
 * Mockito as stated on their website (https://site.mockito.org/):
 * "Do not mock types you do not own."
 */
@RunWith(MockitoJUnitRunner.class)
public class FirebaseAdapterTest {


    private FirebaseAdapter adapter;

    private String testID = "123";
    private String testAssistant = "Assistant";
    private Boolean testIsAssistant = true;

    private final String testTitle = "MyTitle";
    private final String testAddress = "MyAddress";
    private final String testCategory = "MyCategory";
    private final String testSubCategory = "MySubCategory";
    private final String testNotes = "MyNotes";
    private final String ap = "AP";
    private final String testStatus = "MyStatus";
    private final String assistant = "assistant";
    private final String id = "ID";

    private ImmutableTask testTask;
    private String testToken = "123";

    @Before
    public void setUp() throws Exception {
        // Setup our test task and client information
        testTask = ImmutableTask.builder()
                .title(testTitle)
                .address(testAddress)
                .category(testCategory)
                .subCategory(testSubCategory)
                .notes(testNotes)
                .status(testStatus)
                .ap(ap)
                .assistant(assistant)
                .id(id).build();
        ClientInfo.setUsername("test");
        ClientInfo.setCurrentToken("test");
        ClientInfo.setTask(testTask);
        ClientInfo.setIsAssistant(true);
        ClientInfo.setPhoneNumber("0412356789");

        DatabaseReference database;
        database = FirebaseDatabase.getInstance().getReference().child("testing");

        /** Mockito cannot mock final classes? Therefore there's no way to test this. */
//        DataSnapshot snapshot = mock(DataSnapshot.class);
//        when(snapshot.child(anyString())).thenReturn(snapshot);
//        when(snapshot.getKey()).thenReturn("123");
//        when(snapshot.getValue(String.class)).thenReturn(anyString());
//        when(snapshot.getRef()).thenReturn(mock(DatabaseReference.class));
//        when(snapshot.exists()).thenReturn(true);
//        adapter = new FirebaseAdapter(database, snapshot);
        adapter = new FirebaseAdapter();
        adapter.goOffline();
    }

    @After
    public void tearDown() throws Exception {
        adapter = null;
    }

    @Test
    public void pushTask() {
        adapter.pushTask(testTask);
    }

    @Test
    public void updateTask() {
        adapter.updateTask(testTask, testID);
    }

    @Test
    public void updateTaskStatus() {
        adapter.updateTaskStatus(testStatus, testID);
    }

    @Test
    public void updateTaskAssistant() {
        adapter.updateTaskAssistant(testAssistant, testID);
    }

//    @Test
//    public void getCurrentTaskID() {
//        assertThat(adapter.getCurrentTaskID(), instanceOf(String.class));
//    }

//    @Test
//    public void getUser() {
//        assertThat(adapter.getUser(testAssistant), instanceOf(String.class));
//    }

//    @Test
//    public void getIsAssistant() {
//        assertThat(adapter.getIsAssistant(testAssistant), instanceOf(Boolean.class));
//    }

//    @Test
//    public void userExists() {
//        assertThat(adapter.userExists(testAssistant), instanceOf(Boolean.class));
//    }

    @Test
    public void pushUser() {
        adapter.pushUser(testAssistant, testID, testIsAssistant);
    }

//    @Test
//    public void getPhoneNumber() {
//        assertThat(adapter.getPhoneNumber(testAssistant), instanceOf(String.class));
//    }

//    @Test
//    public void getCurrentTask() {
//    }
//
//    @Test
//    public void getTask() {
//    }

//    @Test
//    public void queryCurrentTask() {
//        assertThat(adapter.queryCurrentTask(), instanceOf(Query.class));
//    }

//    @Test
//    public void queryTask() {
//    }

    @Test
    public void pushMessage() {
        adapter.pushMessage(new ChatMessage(testNotes, testAssistant, testAssistant));
    }

    @Test
    public void assignTask() {
        adapter.assignTask(testAssistant, testID);
    }

//    @Test
//    public void completeTask() {
//    }

    @Test
    public void sendRegistrationToServer() {
        adapter.sendRegistrationToServer(testToken);
    }

    @Test
    public void updateRegistrationToServer() {
        adapter.updateRegistrationToServer(testToken, testAssistant);
    }

//    @Test
//    public void getUserRegistration() {
//    }
}