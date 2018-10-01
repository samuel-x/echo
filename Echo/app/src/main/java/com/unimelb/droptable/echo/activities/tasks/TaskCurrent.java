package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ChatActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskCurrent extends AppCompatActivity{

    private TextView taskCurrentTitle;
    private TextView taskCurrentAddress;
    private TextView taskCurrentTime;
    private TextView taskCurrentNotes;
    private TextView otherUserName;
    private TextView otherUserPhone;
    private ConstraintLayout avatar;
    private ConstraintLayout searchingMessage;
    private ImageView messageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_current);

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
            enableAvatar();
        }
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
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // Disable avatar section and begin search for new assistant
                disableAvatar();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                disableAvatar();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                disableAvatar();
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
            otherUserPhone.setText(user.child("phoneNumber").getValue(String.class));
        }
        else {
            user = FirebaseAdapter.getUser(assistantID);
            otherUserName.setText(assistantID);
            otherUserPhone.setText(user.child("phoneNumber").getValue(String.class));
        }

        // enable our avatar
        enableAvatar();
    }

    private void resetAssistant() {
        otherUserName.setText(null);
        otherUserPhone.setText(null);

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
}
