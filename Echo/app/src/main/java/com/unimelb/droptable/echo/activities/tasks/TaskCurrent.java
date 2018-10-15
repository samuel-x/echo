package com.unimelb.droptable.echo.activities.tasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ApMapActivity;
import com.unimelb.droptable.echo.activities.AssistantMapActivity;
import com.unimelb.droptable.echo.activities.ChatActivity;
import com.unimelb.droptable.echo.activities.PaymentActivity;
import com.unimelb.droptable.echo.activities.tasks.uiElements.CompletionTaskDialog;
import com.unimelb.droptable.echo.activities.HelperActivity;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskCurrent extends AppCompatActivity {

    protected TextView taskCurrentTitle;
    protected TextView taskCurrentAddress;
    protected TextView taskCurrentNotes;
    protected TextView taskCurrentPaymentAmount;
    protected TextView otherUserName;
    protected TextView otherUserPhone;
    protected TextView otherUserRating;
    protected ConstraintLayout avatar;
    protected ConstraintLayout searchingMessage;
    protected ImageView messageButton;
    protected ImageView callButton;
    protected FloatingActionButton helperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_current);

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        taskCurrentTitle = findViewById(R.id.textTaskInProgressTitle);
        taskCurrentAddress = findViewById(R.id.textTaskInProgressAddress);
        taskCurrentNotes = findViewById(R.id.textTaskInProgressNotes);
        taskCurrentNotes = findViewById(R.id.textTaskInProgressPaymentAmount);
        otherUserName = findViewById(R.id.userName);
        otherUserPhone = findViewById(R.id.userPhone);
        otherUserRating = findViewById(R.id.userRating);
        avatar = findViewById(R.id.avatarContainer);
        searchingMessage = findViewById(R.id.isReadyLayer);

        messageButton = findViewById(R.id.messagingButton);
        messageButton.setOnClickListener(view -> onMessageButtonClick());

        callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(view -> onCallButtonClick());

        // Get a reference to the helper button and set its listener.
        helperButton = findViewById(R.id.taskCurrentHelperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});

        if (ClientInfo.isAssistant()) {
            // The user is an assistant, and we don't want to display the helper button to them.
            helperButton.setAlpha(0.0f);
            helperButton.setEnabled(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        ImmutableTask currentTask = FirebaseAdapter.getCurrentTask();
        bind(currentTask);
        ClientInfo.setTask(currentTask);

        // Attach our task listener
        if (ClientInfo.isAssistant()) {
            try {
                TaskNotification.AttachAssistantListener(this);
            } catch (TaskNotification.IncorrectListenerException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                TaskNotification.AttachAPListener(this);
            } catch (TaskNotification.IncorrectListenerException e) {
                e.printStackTrace();
            }
        }

        if (ClientInfo.getTask().getAssistant() == null) {
            disableAvatar();
        } else {
            if (ClientInfo.getTask().getStatus().equals("COMPLETED")) {
                TaskNotification.showDialog(this,
                        TaskNotification.TASK_COMPLETE_ASSISTANT_TITLE,
                        TaskNotification.TASK_COMPLETE_ASSISTANT_MESSAGE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User touched the dialog's positive button
                                startActivity(new Intent(TaskCurrent.this,
                                        AssistantMapActivity.class));
                                finish();
                            }
                        });
            }
            enableAvatar();
        }

        // Try to attach a chat listener.
        if (ClientInfo.hasPartner()) {
            MessageNotification.AttachListener(TaskCurrent.this);
        }
    }

    public void bind(@NonNull ImmutableTask task) {
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setNotes(task.getNotes());
        setPaymentAmount(task.getPaymentAmount());
        if (task.getAssistant() != null) {
            updateAssistant(task.getAssistant());
        }
        Log.d("Bind:", "Current Task UI AP");
    }

    private void setTitle(@Nullable String title) {
        taskCurrentTitle.setText(title);
    }

    private void setNotes(@Nullable String notes) {
        taskCurrentNotes.setText(notes);
    }

    private void setPaymentAmount(@Nullable String amount) {
        taskCurrentPaymentAmount.setText(amount);
    }

    private void setAddress(@Nullable String address) {
        taskCurrentAddress.setText(address);
    }

    public void updateAssistant(String assistantID) {
        DataSnapshot user;
        // Update it with the AP's information if we are currently an assistant
        if (ClientInfo.isAssistant()) {
            String AP = ClientInfo.getTask().getAp();
            user = FirebaseAdapter.getUser(AP);
            otherUserName.setText(AP);
            otherUserPhone.setText(user.child(getString(R.string.phone_number_child))
                    .getValue(String.class));
        }
        else {
            user = FirebaseAdapter.getUser(assistantID);
            otherUserName.setText(assistantID);
            otherUserPhone.setText(user.child(getString(R.string.phone_number_child))
                    .getValue(String.class));
            Float rating = user.child("rating").getValue(Float.class);
            rating = rating == null ? 0f : rating;
            otherUserRating.setText("Rating " + rating.toString());
        }

        // enable our avatar
        enableAvatar();
    }

    protected void resetAssistant() {
        otherUserName.setText(getString(R.string.unknown_user));
        otherUserPhone.setText(getString(R.string.empty_phone_number));

        if (ClientInfo.isAssistant()) {
            ClientInfo.setTask(null);
        }
    }

    protected void enableElement(View view) {
        view.setAlpha(1.0f);
        view.setEnabled(true);
    }

    protected void enableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            enableElement(avatar.getChildAt(i));
        }

        for (int i = 0; i < searchingMessage.getChildCount(); i++) {
            disableElement(searchingMessage.getChildAt(i), true);
        }
    }

    public void disableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            disableElement(avatar.getChildAt(i), false);
        }

        for (int i = 0; i < searchingMessage.getChildCount(); i++) {
            enableElement(searchingMessage.getChildAt(i));
        }

        resetAssistant();
    }

    protected void disableElement(View view, boolean zeroOpacity) {
        if (zeroOpacity) {
            view.setAlpha(0.0f);
        }
        else {
            view.setAlpha(0.06f);
        }
        view.setEnabled(false);
    }

    protected void onMessageButtonClick() {
        if (otherUserName.getText().toString().equals(getString(R.string.unknown_user))) {
            // The task has not been accepted and thus there is not user to chat with.
            return;
        }

        startActivity(new Intent(this, ChatActivity.class)
                .putExtra(getString(R.string.chat_partner),
                        (ClientInfo.isAssistant()
                                ? ClientInfo.getTask().getAp()
                                : ClientInfo.getTask().getAssistant())));
    }

    protected void onCallButtonClick() {
        if(otherUserPhone.getText().toString().equals(getString(R.string.empty_phone_number))){
            // Assistant has no phone number so do nothing
            return;
        }else {
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                //Permission is allowed so call is made
                makeCall();
            } else {
                //Permission is denied so permission is requested and then call is made
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 0);
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                        == PackageManager.PERMISSION_GRANTED) {
                    //Permission has been granted
                    makeCall();
                } else {
                    //Permission denied
                    return;
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + otherUserPhone.getText().toString()));
        startActivity(intent);
    }

    protected void onHelperPress() {
        HelperActivity.setCurrentHelperText(getString(R.string.task_current_helper_text));
        startActivity(new Intent(this, HelperActivity.class));
    }
}
