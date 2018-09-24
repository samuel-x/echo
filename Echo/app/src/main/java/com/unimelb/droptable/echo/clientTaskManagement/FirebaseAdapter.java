package com.unimelb.droptable.echo.clientTaskManagement;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAdapter {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    private final static DatabaseReference dbRef = database.getReference();

    public static void pushTask(ImmutableTask task) {
        dbRef.child("tasks").setValue(task);
    }
}
