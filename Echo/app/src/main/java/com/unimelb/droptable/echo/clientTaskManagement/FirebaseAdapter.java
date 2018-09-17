package com.unimelb.droptable.echo.clientTaskManagement;


import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseAdapter {

    final static FirebaseDatabase database = FirebaseDatabase.getInstance();

    public static void pushTask(Task task) {
        DatabaseReference dbRef = database.getReference();
        dbRef.child("tasks").setValue(task);
    }
}
