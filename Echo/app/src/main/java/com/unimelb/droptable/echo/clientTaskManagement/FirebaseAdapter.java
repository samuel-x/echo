package com.unimelb.droptable.echo.clientTaskManagement;


import android.util.Log;

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

    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final static DatabaseReference masterDbReference = database.getReference();
    public final static DatabaseReference tasksDbReference = database.getReference().child("tasks");
    public final static DatabaseReference messagesDbReference = database.getReference().child("messages");

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
    public final static Query mostRecentTasks = tasksDbReference.limitToLast(10);

    /**
     * Pushes a task to the database and assigns it to the current user
     * @param task
     * @return
     */
    public static int pushTask(ImmutableTask task) {
        DatabaseReference pushTask =tasksDbReference.push();
        pushTask.setValue(task);

        DatabaseReference pushUser = masterDbReference.child("users").child(ClientInfo.getUsername());
        pushUser.child("taskID").setValue(pushTask.getKey());
        pushUser.child("isAssistant").setValue(false);

        return HttpURLConnection.HTTP_OK;
    }

    public static String getCurrentTaskID() {
        return currentData.child("users").child(ClientInfo.getUsername())
                .child("taskID").getValue(String.class);
    }

    /**
     * Get current user's task once (if they have one)
     */
    public static ImmutableTask getCurrentTask() {
        return getTask(getCurrentTaskID());
    }

    public static ImmutableTask getTask(String id) {
        String title = currentData.child("tasks").child(id).child("title").getValue(String.class);
        String address = currentData.child("tasks").child(id).child("address").getValue(String.class);
        String category = currentData.child("tasks").child(id).child("category").getValue(String.class);
        String subCategory = currentData.child("tasks").child(id).child("subCategory").getValue(String.class);
        String notes = currentData.child("tasks").child(id).child("notes").getValue(String.class);

        return ImmutableTask.builder()
                .title(title)
                .address(address)
                .category(category)
                .subCategory(subCategory)
                .notes(notes).build();
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
        masterDbReference.addValueEventListener(FirebaseAdapter.listener);
    }

    public static void pushMessage(ChatMessage message) {
        messagesDbReference.child(Utility.generateUserChatId(message.getSender(),
                message.getReceiver())).push().setValue(message);
    }
}
