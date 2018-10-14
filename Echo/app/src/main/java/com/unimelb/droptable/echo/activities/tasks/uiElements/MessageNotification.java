package com.unimelb.droptable.echo.activities.tasks.uiElements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;


public class MessageNotification extends DialogFragment {

    // Messages that have a timestamp older than this many milliseconds are not displayed.
    private static final int NOTIFICATION_TIMEOUT = 10 * 1000;

    private static Set<Activity> attachedListeners = new HashSet<>();

    public static void showDialog(Context context, String sender, String message) {
        if (!((Activity) context).hasWindowFocus()) {
            // This window is not in focus, so don't try to show.
            return;
        }

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(context,
                    android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(context);
        }
        builder.setTitle("Message received")
                .setMessage(String.format("%s says: %s", sender, message))
                .setPositiveButton(android.R.string.yes,
                        (dialog, which) -> {

                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void AttachListener(Activity activity) {
        if (attachedListeners.contains(activity)) {
            // Given activity already has a listener.
            return;
        }

        if (!ClientInfo.hasPartner()) {
            // Client doesn't have a partner, so no chat is active.
            return;
        }

        Query chatQuery = FirebaseAdapter.queryCurrentChat();
        chatQuery.addChildEventListener(createChatListener(activity));
        attachedListeners.add(activity);
    }


    private static ChildEventListener createChatListener(Activity activity) {
        return new ChildEventListener() {

            // Check that the added value is an assistant and show the dialog
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                HashMap<String, Object> data = ((HashMap<String, Object>) dataSnapshot.getValue());

                if (new Date().getTime() - NOTIFICATION_TIMEOUT
                        > (Long) data.get("messageTime")) {
                    // The message happened a while ago, don't display.
                    return;
                }

                if (ClientInfo.getUsername().equals(data.get("receiver"))) {
                    MessageNotification.showDialog(activity, (String) data.get("sender"),
                            (String) data.get("messageText"));
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }
}