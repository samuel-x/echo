package com.unimelb.droptable.echo.clientTaskManagement;


import android.util.Log;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Arrays;

public class FirebaseAdapter {

    private static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private static DatabaseReference dbRef = database.getReference();

    // Our base query
    public final static Query mostRecentTasks = dbRef.child("tasks_dev").limitToLast(10);

    /**
     * Pushes a task to the database
     * @param task
     * @return
     */
    public static int pushTask(ImmutableTask task) {
        DatabaseReference push = dbRef.child("tasks_dev").push();
        push.setValue(task);
        return HttpURLConnection.HTTP_OK;
    }

    /**
     * Pulls all tasks from the database
     * @return
     */
    public static int pullTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot task : dataSnapshot.getChildren()) {
                    ImmutableTask newTask = task.getValue(ImmutableTask.class);
                    tasks.add(newTask);
                };

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Log.d("Pull", Arrays.toString(tasks.toArray()));

        return HttpURLConnection.HTTP_OK;
    }

    public static void goOffline() {
        database.goOffline();
    }

    public static void goOnline() {
        database.goOnline();
    }
}
