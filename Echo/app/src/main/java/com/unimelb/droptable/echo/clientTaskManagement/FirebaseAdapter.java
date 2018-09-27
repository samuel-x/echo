package com.unimelb.droptable.echo.clientTaskManagement;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.unimelb.droptable.echo.ChatMessage;

public class FirebaseAdapter {

    public final static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public final static DatabaseReference masterDbReference = database.getReference();
    public final static DatabaseReference tasksDbReference = database.getReference().child("tasks");
    public final static DatabaseReference messagesDbReference = database.getReference().child("messages");

    public static void pushTask(ImmutableTask task) {
        tasksDbReference.setValue(task);
    }

    public static void pushMessage(ChatMessage message) {
        messagesDbReference.child(Utility.generateUserChatId(message.getSender(),
                message.getReceiver())).push().setValue(message);
    }
}
