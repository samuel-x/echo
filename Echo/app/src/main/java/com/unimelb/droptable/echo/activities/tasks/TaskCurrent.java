package com.unimelb.droptable.echo.activities.tasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
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
import com.unimelb.droptable.echo.activities.tasks.uiElements.CancelledTaskDialog;
import com.unimelb.droptable.echo.activities.tasks.uiElements.CompletedTaskDialog;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskCurrent extends AppCompatActivity
        implements CancelledTaskDialog.NoticeDialogListener, CompletedTaskDialog.NoticeDialogListener{

    private TextView taskCurrentTitle;
    private TextView taskCurrentAddress;
    private TextView taskCurrentTime;
    private TextView taskCurrentNotes;
    private TextView otherUserName;
    private TextView otherUserPhone;
    private ConstraintLayout avatar;
    private ConstraintLayout searchingMessage;
    private ImageView messageButton;
    private ImageView callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_current);

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        taskCurrentTitle = findViewById(R.id.textTaskInProgressTitle);
        taskCurrentAddress = findViewById(R.id.textTaskInProgressAddress);
        taskCurrentTime = findViewById(R.id.textTaskInProgressTime);
        taskCurrentNotes = findViewById(R.id.textTaskInProgressNotes);
        otherUserName = findViewById(R.id.userName);
        otherUserPhone = findViewById(R.id.userPhone);
        avatar = findViewById(R.id.avatarContainer);
        searchingMessage = findViewById(R.id.isReadyLayer);

        messageButton = findViewById(R.id.messagingButton);
        messageButton.setOnClickListener(view -> onMessageButtonClick());

        callButton = findViewById(R.id.callButton);
        callButton.setOnClickListener(view -> onCallButtonClick());
    }

    @Override
    protected void onStart() {
        super.onStart();

        ChildEventListener childEventListener = createListener();
        Query query = FirebaseAdapter.queryCurrentTask();
        query.addChildEventListener(childEventListener);
        ImmutableTask currentTask = FirebaseAdapter.getCurrentTask();
        bind(currentTask);
        ClientInfo.setTask(currentTask);

        if (ClientInfo.getTask().getAssistant() == null) {
            disableAvatar();
        } else {
            if (ClientInfo.getTask().getStatus().equals("COMPLETED")) {
                showCompletedDialog();
            }
            enableAvatar();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        if (ClientInfo.getTask().getStatus().equals("COMPLETED")) {
            showCompletedDialog();
        }
    }

    public void showCompletedDialog() {
        DialogFragment dialog = new CompletedTaskDialog();
        dialog.show(getSupportFragmentManager(), "CompletedDialogFragment");
    }

    public void showCancelledDialog() {
        // Create an instance of the dialog fragment and show it
        DialogFragment dialog = new CancelledTaskDialog();
        dialog.show(getSupportFragmentManager(), "CancelledDialogFragment");
    }

    // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // User touched the dialog's positive button
        if (dialog.getTag().equals("CompletedDialogFragment")) {
            if (ClientInfo.isAssistant()) {
                startActivity(new Intent(this, AssistantMapActivity.class));
                ClientInfo.setTask(null);
            }
            else {
                startActivity(new Intent(this, PaymentActivity.class));
            }
        }
        else {
            if (ClientInfo.isAssistant()) {
                startActivity(new Intent(this, AssistantMapActivity.class));
                ClientInfo.setTask(null);
            }
            else {
                startActivity(new Intent(this, ApMapActivity.class));
                ClientInfo.setTask(null);
            }
        }
        finish();
    }

    public void bind(@NonNull ImmutableTask task) {
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setNotes(task.getNotes());
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

    private void setAddress(@Nullable String address) {
        taskCurrentAddress.setText(address);
    }

    private ChildEventListener createListener() {
        return new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                // Check if the new child added is an assistant, if so, then update our avatar card
                if (dataSnapshot.getKey().toString().equals("assistant")) {
                    String assistantID = dataSnapshot.getValue(String.class);
                    updateAssistant(assistantID);
                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Check if the new child added is an assistant, if so, then update our avatar card
                if (dataSnapshot.getKey().toString().equals("assistant")) {
                    String assistantID = dataSnapshot.getValue(String.class);
                    updateAssistant(assistantID);
                }
                if (dataSnapshot.getKey().toString().equals("status") && dataSnapshot.getValue(String.class).equals("COMPLETED")) {
                    showCompletedDialog();
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Disable avatar section and begin search for new assistant
                showCancelledDialog();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                disableAvatar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                showCancelledDialog();
            }
        };
    }

    private void updateAssistant(String assistantID) {
        DataSnapshot user;
        // Update it with the AP's information if we are currently an assistant
        if (ClientInfo.isAssistant()) {
            String AP = ClientInfo.getTask().getAp();
            user = FirebaseAdapter.getUser(AP);
            otherUserName.setText(AP);
            otherUserPhone.setText(user.child(getString(R.string.phone_number_child)).getValue(String.class));
        }
        else {
            user = FirebaseAdapter.getUser(assistantID);
            otherUserName.setText(assistantID);
            otherUserPhone.setText(user.child(getString(R.string.phone_number_child)).getValue(String.class));
        }

        // enable our avatar
        enableAvatar();
    }

    private void resetAssistant() {
        otherUserName.setText(getString(R.string.unknown_user));
        otherUserPhone.setText(getString(R.string.empty_phone_number));

        if (ClientInfo.isAssistant()) {
            ClientInfo.setTask(null);
        }
    }

    private void enableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            avatar.getChildAt(i).setAlpha(1.0f);
        }

        for (int i = 0; i < searchingMessage.getChildCount(); i++) {
            searchingMessage.getChildAt(i).setEnabled(false);
            searchingMessage.getChildAt(i).setAlpha(0.0f);
        }
    }

    private void disableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            avatar.getChildAt(i).setAlpha(0.06f);
        }

        for (int i = 0; i < searchingMessage.getChildCount(); i++) {
            searchingMessage.getChildAt(i).setEnabled(true);
            searchingMessage.getChildAt(i).setAlpha(1.0f);
        }

        resetAssistant();
    }

    private void onMessageButtonClick() {

        startActivity(new Intent(this, ChatActivity.class)
                .putExtra(getString(R.string.chat_partner),
                        (ClientInfo.isAssistant()
                                ? ClientInfo.getTask().getAp()
                                : ClientInfo.getTask().getAssistant())));
    }

    private void onCallButtonClick() {
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
    private void makeCall() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + otherUserPhone.getText().toString()));
        startActivity(intent);
    }
}
