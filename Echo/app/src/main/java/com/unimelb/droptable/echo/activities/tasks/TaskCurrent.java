package com.unimelb.droptable.echo.activities.tasks;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.R;
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

    }

    @Override
    protected void onStart() {
        super.onStart();
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
                String assistantID = dataSnapshot.getValue(String.class);
                updateAssistant(assistantID);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                // Update our element
                String assistantID = dataSnapshot.getValue(String.class);
                updateAssistant(assistantID);
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

        // enable our avatar
        enableAvatar();
    }

    private void enableAvatar() {

        avatar.setAlpha(1.0f);
        searchingMessage.setEnabled(false);

    }

    private void disableAvatar() {

        avatar.setAlpha(0.1f);
        searchingMessage.setEnabled(true);

    }
}
