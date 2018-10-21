package com.unimelb.droptable.echo.activities.tasks.uiElements;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ApMapActivity;
import com.unimelb.droptable.echo.activities.AssistantMapActivity;
import com.unimelb.droptable.echo.activities.PaymentActivity;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

import java.util.HashSet;
import java.util.Set;


public class TaskNotification extends DialogFragment {

    public static final String TASK_ACCEPT_TITLE = "Task Accepted!";
    public static final String TASK_ACCEPT_MESSAGE = "Your task has been accepted by ";
    public static final String TASK_COMPLETE_REQUEST_TITLE = "Complete Task Request";
    public static final String TASK_COMPLETE_REQUEST_MESSAGE = "Task Completion has been requested by ";
    public static final String TASK_CANCELLED_TITLE = "Task Cancellation";
    public static final String TASK_CANCELLED_MESSAGE = "Unfortunately, the task has been cancelled.";
    public static final String TASK_COMPLETE_ASSISTANT_TITLE = "Task Complete!";
    public static final String TASK_COMPLETE_ASSISTANT_MESSAGE = "The task was accepted by the AP!";
    public static final String TASK_PHASE_ONE_COMPLETE_TITLE = "Task updated!";
    public static final String TASK_PHASE_ONE_COMPLETE_MESSAGE = "The task has been updated.";

    private static Set<Activity> attachedListeners = new HashSet<>();

    public static void showDialog(Context context, String title, String message) {
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
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes,
                        (dialog, which) -> {

                        })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void showDialog(Context context, String title, String message,
                                  DialogInterface.OnClickListener option) {
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
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes, option)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    public static void AttachAPListener(Activity activity) throws IncorrectListenerException {
        if (attachedListeners.contains(activity)) {
            // Given activity already has a listener.
            return;
        }

        if (!ClientInfo.isAssistant()) {
            Query taskQuery = FirebaseAdapter.queryCurrentTask();
            taskQuery.addChildEventListener(createAPTaskListener(activity));
            attachedListeners.add(activity);
        } else {
            throw new IncorrectListenerException("Error attaching listener: Expected AP, " +
                    "but user is assistant.");
        }

    }

    public static void AttachAssistantListener(Activity activity) throws IncorrectListenerException
    {
        if (attachedListeners.contains(activity)) {
            // Given activity already has a listener.
            return;
        }

        if (ClientInfo.isAssistant()) {
            Query taskQuery = FirebaseAdapter.queryCurrentTask();
            taskQuery.addChildEventListener(createAssistantTaskListener(activity));
            attachedListeners.add(activity);
        }
        else {
            throw new IncorrectListenerException("Error attaching listener: Expected assistant, " +
                    "but user is AP.");
        }
    }

    /**
     * Create a listener to listen to changes on the task. If a task is removed, then show the
     * necessary dialog.
     * @return
     */
    protected static ChildEventListener createAssistantTaskListener(Activity activity) {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                ClientInfo.updateTask();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                ClientInfo.updateTask();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // If our task is completed, account for each activity we may be in
                if (dataSnapshot.getKey().toString().equals("title")) {
                    ClientInfo.setTask(null);

                    if (activity instanceof AssistantMapActivity) {
                        // If we are in the assistant map activity, just show the dialog and disable
                        // the complete task button.
                        AssistantMapActivity assistantActivity = (AssistantMapActivity) activity;
                        assistantActivity.disableCompleteTask();
                        assistantActivity.taskButton.setText(R.string.new_task_home_button);
                        TaskNotification.showDialog(activity, TASK_COMPLETE_ASSISTANT_TITLE,
                                TASK_COMPLETE_ASSISTANT_TITLE);
                    } else if (activity instanceof TaskCurrent) {
                        // If we are in the CurrentTask activity, then disable the avatar and
                        // show the dialog, returning to the AssistantMapActivity when accepting.
                        TaskCurrent currentActivity = (TaskCurrent) activity;
                        currentActivity.disableAvatar();
                        TaskNotification.showDialog(activity, TASK_COMPLETE_ASSISTANT_TITLE,
                                TASK_COMPLETE_ASSISTANT_TITLE,
                                (dialog, which) -> {
                                    // User touched the dialog's positive button
                                    activity.startActivity(new Intent(activity,
                                            AssistantMapActivity.class));
                                    activity.finish();
                                });
                    } else {
                        // Otherwise, just show the dialog and return to the AssistantMapActivity.
                        TaskNotification.showDialog(activity, TASK_COMPLETE_ASSISTANT_TITLE,
                                TASK_COMPLETE_ASSISTANT_TITLE,
                                (dialog, which) -> {
                                    // User touched the dialog's positive button
                                    activity.startActivity(new Intent(activity,
                                            AssistantMapActivity.class));
                                    activity.finish();
                                });
                    }

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                ClientInfo.updateTask();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }

    protected static ChildEventListener createAPTaskListener(Activity activity) {
        return new ChildEventListener() {

            // Check that the added value is an assistant and show the dialog
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot != null &&
                        dataSnapshot.getValue(String.class) != null &&
                        dataSnapshot.getKey().toString().equals("assistant")) {
                    String assistantID = dataSnapshot.getValue(String.class);
                    String message = TASK_ACCEPT_MESSAGE + assistantID + "!";
                    TaskNotification.showDialog(activity, TASK_ACCEPT_TITLE, message);

                    // If we're in the TaskCurrent activity, enable our avatar
                    if (activity instanceof TaskCurrent) {
                        TaskCurrent currentActivity = (TaskCurrent) activity;
                        currentActivity.updateAssistant(assistantID);
                    }

                    // Update task with assistant.
                    ClientInfo.updateAssistant(assistantID);
                }
            }

            // Check that the task has been completed.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot != null &&
                        dataSnapshot.getValue(String.class) != null &&
                        dataSnapshot.getKey().toString().equals("status") &&
                        dataSnapshot.getValue(String.class).equals("COMPLETED")) {

                    String assistantID = ClientInfo.getTask().getAssistant();
                    String message = TASK_COMPLETE_REQUEST_MESSAGE + assistantID + "!";
                    TaskNotification.showDialog(activity, TASK_COMPLETE_REQUEST_TITLE, message,
                            (dialog, which) -> {
                                // User touched the dialog's positive button
                                activity.startActivity(new Intent(activity,
                                        PaymentActivity.class));
                                activity.finish();
                            });
                }
                if (dataSnapshot != null &&
                        dataSnapshot.getValue(String.class) != null &&
                        dataSnapshot.getKey().toString().equals("lastPhase") &&
                        dataSnapshot.getValue(String.class).equals("true")) {

                    String assistantID = ClientInfo.getTask().getAssistant();
                    TaskNotification.showDialog(activity, TASK_PHASE_ONE_COMPLETE_TITLE,
                            TASK_PHASE_ONE_COMPLETE_MESSAGE,
                            (dialog, which) -> {
                            });
                }
            }

            // If a task has been removed, show an error.
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().toString().equals("title")) {
                    TaskNotification.showDialog(activity, TASK_CANCELLED_TITLE,
                            TASK_CANCELLED_MESSAGE, (dialog, which) -> {
                                // User touched the dialog's positive button
                                activity.startActivity(new Intent(activity,
                                        ApMapActivity.class));
                                activity.finish();
                            });
                    ClientInfo.setTask(null);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }

    public static class IncorrectListenerException extends Exception {
        public IncorrectListenerException() {
            super();
        }
        public IncorrectListenerException(String message) {
            super(message);
        }
    }
}