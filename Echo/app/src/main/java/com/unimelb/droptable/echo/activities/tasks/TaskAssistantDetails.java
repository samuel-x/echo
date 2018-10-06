package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.AssistantMapActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class TaskAssistantDetails extends AppCompatActivity {

    private TextView taskCurrentTitle;
    private TextView taskCurrentAddress;
    private TextView taskCurrentTime;
    private TextView taskCurrentNotes;
    private Button acceptButton;
    private ImmutableTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_assistant_accept);
        task = (ImmutableTask) getIntent().getSerializableExtra("task");
        taskCurrentTitle = findViewById(R.id.textAssistantTaskListActivity);
        taskCurrentAddress = findViewById(R.id.textAssistantTaskListAddress);
        taskCurrentTime = findViewById(R.id.textAssistantTaskListTime);
        taskCurrentNotes = findViewById(R.id.textAssistantTaskListNotes);
        acceptButton = findViewById(R.id.buttonAcceptTask);

        acceptButton.setOnClickListener((view) -> onAccept());
    }

    @Override
    protected void onStart(){
        super.onStart();
        setTitle(task.getTitle());
        setAddress(task.getAddress());
        setNotes(task.getNotes());
        Log.d("Bind:", "what the heck");
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

    private void onAccept() {
        FirebaseAdapter.assignTask(ClientInfo.getUsername(), task.getId());
        ClientInfo.setTask(task);
        startActivity(new Intent(this, AssistantMapActivity.class));
        finish();
    }
}

