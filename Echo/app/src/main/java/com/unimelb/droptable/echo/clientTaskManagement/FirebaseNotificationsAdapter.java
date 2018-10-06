package com.unimelb.droptable.echo.clientTaskManagement;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

import static com.unimelb.droptable.echo.clientTaskManagement.Utility.generateUserChatId;

public class FirebaseNotificationsAdapter {
    public static FirebaseMessaging fm = FirebaseMessaging.getInstance();

    public static void sendMessage(String recipientID, String message, String senderID) {
        String msgId = generateUserChatId(recipientID, senderID);
        fm.send(new RemoteMessage.Builder(recipientID + "@gcm.googleapis.com")
                .setMessageId(msgId + System.currentTimeMillis())
                .addData("my_message", "Hello World")
                .addData("my_action","SAY_HELLO")
                .addData("from", senderID)
                .addData("to", recipientID)
                .addData("body", message)
                .build());
    }
}
