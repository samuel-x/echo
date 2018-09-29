package com.unimelb.droptable.echo.activities.tasks;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
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
import com.unimelb.droptable.echo.activities.ChatActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;


public class TaskCurrent extends AppCompatActivity{

    private TextView taskCurrentTitle;
    private TextView taskCurrentAddress;
    private TextView taskCurrentTime;
    private TextView taskCurrentNotes;
    private TextView assistantName;
    private TextView assistantPhone;
    private ConstraintLayout avatar;
    private ConstraintLayout searchingMessage;
    private ImageView messageButton;
    private ImageView callButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_current);

        taskCurrentTitle = findViewById(R.id.textTaskInProgressTitle);
        taskCurrentAddress = findViewById(R.id.textTaskInProgressAddress);
        taskCurrentTime = findViewById(R.id.textTaskInProgressTime);
        taskCurrentNotes = findViewById(R.id.textTaskInProgressNotes);
        assistantName = findViewById(R.id.userName);
        assistantPhone = findViewById(R.id.userPhone);
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

        if (ClientInfo.getTask().getAssistant().isEmpty()) {
            disableAvatar();
        }
        else {
            enableAvatar();
        }
        ChildEventListener childEventListener = createListener();
        Query query = FirebaseAdapter.queryCurrentTask();
        query.addChildEventListener(childEventListener);
        ImmutableTask currentTask = FirebaseAdapter.getCurrentTask();
        bind(currentTask);
    }

    public void bind(@NonNull ImmutableTask task) {
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setNotes(task.getNotes());
        if (!task.getAssistant().isEmpty()) {
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
        // TODO: pull data from firebase about the assistant
        assistantName.setText(assistantID);
        assistantPhone.setText("0412345678"); //Default number, TODO: pull number from firebase
        // enable our avatar
        enableAvatar();
    }

    private void enableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            avatar.getChildAt(i).setAlpha(1.0f);
        }

        searchingMessage.setEnabled(false);

    }

    private void disableAvatar() {
        for (int i = 0; i < avatar.getChildCount(); i++) {
            avatar.getChildAt(i).setAlpha(0.06f);
        }

        searchingMessage.setEnabled(true);
    }

    private void onMessageButtonClick() {

        startActivity(new Intent(this, ChatActivity.class)
                .putExtra(getString(R.string.chat_partner),
                        (ClientInfo.isAssistant()
                                ? ClientInfo.getTask().getAp()
                                : ClientInfo.getTask().getAssistant())));
    }

    private void onCallButtonClick() {
        if(assistantPhone.getText().toString().equals(getString(R.string.empty_phone_number))){
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
        intent.setData(Uri.parse("tel:" + assistantPhone.getText().toString()));
        startActivity(intent);
    }
}
