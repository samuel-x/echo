package com.unimelb.droptable.echo.clientTaskManagement;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseAdapter {

    private final static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void pushTask(ImmutableTask task) {
        DatabaseReference dbRef = database.getReference();
        dbRef.child("tasks").setValue(task);
    }
}
