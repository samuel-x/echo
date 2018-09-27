package com.unimelb.droptable.echo.clientTaskManagement;


import android.util.Log;

import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unimelb.droptable.echo.ClientInfo;

import java.net.HttpURLConnection;
import java.util.ArrayList;

public class FirebaseAdapter {

    public static DataSnapshot currentData;
    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final static DatabaseReference dbRef = database.getReference();
    public static DatabaseReference dbRefUsers = dbRef.child("users");
    public static DatabaseReference dbRefTasks = dbRef.child("tasks_dev");

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
    public final static Query mostRecentTasks = dbRef.child("tasks_dev").limitToLast(10);

    /**
     * Pushes a task to the database and assigns it to the current user
     * @param task
     * @return
     */
    public static int pushTask(ImmutableTask task) {
        DatabaseReference pushTask = dbRef.child("tasks_dev").push();
        pushTask.setValue(task);

        DatabaseReference pushUser = dbRef.child("users").child(ClientInfo.getUsername());
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
        String title = currentData.child("tasks_dev").child(id).child("title").getValue(String.class);
        String address = currentData.child("tasks_dev").child(id).child("address").getValue(String.class);
        String category = currentData.child("tasks_dev").child(id).child("category").getValue(String.class);
        String subCategory = currentData.child("tasks_dev").child(id).child("subCategory").getValue(String.class);
        String notes = currentData.child("tasks_dev").child(id).child("notes").getValue(String.class);



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
        Query task = dbRef.child("tasks_dev").child(id);

        return task;
    }

    public static void goOffline() {
        database.goOffline();
    }

    public static void goOnline() {
        dbRef.addValueEventListener(FirebaseAdapter.listener);
    }
}
