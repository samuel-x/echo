package com.unimelb.droptable.echo.clientTaskManagement;


import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unimelb.droptable.echo.ChatMessage;
import com.unimelb.droptable.echo.ClientInfo;

import java.net.HttpURLConnection;

public class FirebaseAdapter {

    // TODO: Figure out a way to use the strings.xml resources here. Right now, it crashes if used.
//    private final static String TASKS_ROOT = Resources.getSystem().getString(R.string.tasks_root);

    //            = Resources.getSystem().getString(R.string.user_is_assistant);
    //    private final static String IS_ASSISTANT
//    private final static String TASK_ID = Resources.getSystem().getString(R.string.task_id);
//    private final static String USERS_ROOT = Resources.getSystem().getString(R.string.users_root);
//    private final static String MESSAGES_ROOT
//            = Resources.getSystem().getString(R.string.messages_root);

    private final static String TASKS_ROOT = "tasks";
    private final static String MESSAGES_ROOT = "messages";
    private final static String USERS_ROOT = "users";
    private final static String TASK_ID = "taskID";
    private final static String IS_ASSISTANT = "isAssistant";
    private final static String PHONE_NUMBER = "phoneNumber";

    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final static DatabaseReference masterDbReference = database.getReference();
    public final static DatabaseReference tasksDbReference = database.getReference()
            .child(TASKS_ROOT);
    public final static DatabaseReference messagesDbReference = database.getReference()
            .child(MESSAGES_ROOT);
    public final static DatabaseReference usersDbReference = database.getReference()
            .child(USERS_ROOT);

    public static DataSnapshot currentData;

    public static ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            FirebaseAdapter.currentData = snapshot;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
            Log.d("Error:", "Error reading task from Database");
        }
    };

    // Our base query for assistants
    public final static Query mostRecentTasks = tasksDbReference.orderByChild("status").equalTo("PENDING");

    /**
     * Pushes a task to the database and assigns it to the current user
     * @param task
     * @return
     */
    public static int pushTask(ImmutableTask task) {
        DatabaseReference pushTask = tasksDbReference.push();
        pushTask.setValue(task);
        pushTask.child("id").setValue(pushTask.getKey());

        usersDbReference.child(ClientInfo.getUsername()).child(TASK_ID).setValue(pushTask.getKey());

        // TODO: Make the code returned actually reflect the true status.
        return HttpURLConnection.HTTP_OK;
    }

    /**
     * Compares the specified task to the task on Firebase and updates changes accordingly
     * @param task
     * @param id
     * @return
     */
    public static int updateTask(ImmutableTask task, String id) {
        DatabaseReference push = tasksDbReference.child(id);
        push.setValue(task);
        return HttpURLConnection.HTTP_OK;
    }

    /**
     * Updates the status of a task
     * @return
     */
    public static int updateTaskStatus(String newStatus, String id) {
        tasksDbReference.child(id).child("status").setValue(newStatus);
        return HttpURLConnection.HTTP_OK;
    }

    /**
     * Updates the assistant id of a task
     * @return
     */
    public static int updateTaskAssistant(String newAssistant, String id) {
        tasksDbReference.child(id).child("assistant").setValue(newAssistant);
        return HttpURLConnection.HTTP_OK;
    }

    public static String getCurrentTaskID() {
        return currentData.child(USERS_ROOT).child(ClientInfo.getUsername())
                .child(TASK_ID).getValue(String.class);
    }

    public static Boolean getIsAssistant(String username) {
        if (!currentData.child(USERS_ROOT).hasChild(username)) {
            return null;
        }

        return currentData
                .child(USERS_ROOT)
                .child(username)
                .child(IS_ASSISTANT)
                .getValue(Boolean.class);
    }

    public static boolean userExists(String username) {
        return currentData
                .child(USERS_ROOT)
                .hasChild(username);
    }

    public static int pushUser(String username, boolean isAssistant) {
        usersDbReference.child(username).child(IS_ASSISTANT).setValue(isAssistant);

        // TODO: Make the code returned actually reflect the true status.
        return HttpURLConnection.HTTP_OK;
    }

    public static int pushPhoneNumber(String username,String phoneNumber){
        usersDbReference.child(username).child(PHONE_NUMBER).setValue(phoneNumber);
        System.out.println(getPhoneNumber(username));
        // TODO: Make the code returned actually reflect the true status.
        return HttpURLConnection.HTTP_OK;
    }

    public static String getPhoneNumber(String username) {
        if (!currentData.child(USERS_ROOT).hasChild(username)) {
            return null;
        }

        if(!currentData.child(USERS_ROOT).child(username).hasChild(PHONE_NUMBER)){
            return null;
        }

        return currentData
                .child(USERS_ROOT)
                .child(username)
                .child(PHONE_NUMBER)
                .getValue(String.class);

    }

    /**
     * Get current user's task once (if they have one)
     */
    public static ImmutableTask getCurrentTask() {
        String currentTaskId = getCurrentTaskID();

        if (currentTaskId == null) {
            // This user does not have any task assigned to them.
            return null;
        }

        return getTask(currentTaskId);
    }

    public static ImmutableTask getTask(String id) {
        DataSnapshot taskRef = currentData.child(TASKS_ROOT).child(id);

        // TODO: Change the strings here for ImmutableTask to use strings.xml.
        String title = taskRef.child("title").getValue(String.class);

        if (title == null) {
            // This task does not exist, so return null.
            return null;
        }

        String address = taskRef.child("address").getValue(String.class);
        String category = taskRef.child("category").getValue(String.class);
        String subCategory = taskRef.child("subCategory").getValue(String.class);
        String notes = taskRef.child("notes").getValue(String.class);
        String status = taskRef.child("status").getValue(String.class);
        String ap = taskRef.child("ap").getValue(String.class);
        String assistant = taskRef.child("assistant").getValue(String.class);


        return ImmutableTask.builder()
                .title(title)
                .address(address)
                .category(category)
                .subCategory(subCategory)
                .notes(notes)
                .status(status)
                .ap(ap)
                .assistant(assistant)
                .id(taskRef.getKey()).build();
    }

    /**
     * Returns a Query for the current user's task
     * @return Query
     */
    public static Query queryCurrentTask() {
        return queryTask(getCurrentTaskID());
    }

    /**
     * Returns a Query for a specified task id
     * @return
     */
    public static Query queryTask(String id) {
        return tasksDbReference.child(id);
    }

    public static void goOffline() {
        database.goOffline();
    }

    public static void goOnline() {
        masterDbReference.addListenerForSingleValueEvent(FirebaseAdapter.listener);
        masterDbReference.addValueEventListener(FirebaseAdapter.listener);
    }

    public static void pushMessage(ChatMessage message) {
        messagesDbReference.child(Utility.generateUserChatId(message.getSender(),
                message.getReceiver())).push().setValue(message);
    }

    public static void assignTask(String assistant, String id) {
        updateTaskStatus("ACCEPTED", id);
        updateTaskAssistant(assistant, id);
        updateAssistantTask(assistant, id);
    }

    private static void updateAssistantTask(String assistant, String id) {
        usersDbReference.child(assistant).child("taskID").setValue(id);
    }

}
