package com.unimelb.droptable.echo.clientTaskManagement;

import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

public class FirebaseNotificationsAdapter {
    public static FirebaseMessaging fm = FirebaseMessaging.getInstance();

//    public static void sendMessage(String recipientID, String senderID, String message) {
//        String msgId = generateUserChatId(recipientID, senderID);
//        fm.send(new RemoteMessage.Builder(recipientID + "@gcm.googleapis.com")
//                .setMessageId(msgId + System.currentTimeMillis())
//                .addData("my_message", "Hello World")
//                .addData("my_action","SAY_HELLO")
//                .addData("from", senderID)
//                .addData("to", recipientID)
//                .addData("body", message)
//                .build());
//    }

    public static void sendAssistantCompleteMessage(ImmutableTask task) {
        String recipient = FirebaseAdapter.getUserRegistration(task.getAssistant());
        String sender = FirebaseAdapter.getUserRegistration(task.getAp());
        String message = "Task " + task.getTitle() + " has been completed!";
        Log.d("MESSAGING: ", "Recipient ID: " + recipient);
        Log.d("MESSAGING: ", "Sender ID: " + sender);
//        sendMessage(recipient, sender, message);
    }
}
