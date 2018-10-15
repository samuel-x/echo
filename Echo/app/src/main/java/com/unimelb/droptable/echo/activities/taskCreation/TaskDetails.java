package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

public class TaskDetails extends AppCompatActivity {

    // Grab UI references
    protected TextView title;
    protected TextView address;
    protected TextView taskNotes;
    protected Button submitNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_details);

        // Setup our buttons
        title = findViewById(R.id.textTaskTitle);
        address = findViewById(R.id.textTaskAddress);
        taskNotes = findViewById(R.id.textTaskNotes);
        submitNowButton = findViewById(R.id.buttonTaskNow);

        submitNowButton.setOnClickListener((view) -> {
            onContinue();});

    }

    protected void onContinue() {
        Utility.currentTaskBuilder.title(title.getText().toString());
        Utility.currentTaskBuilder.address(address.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        startActivity(new Intent(this, TaskConfirm.class));
    }
}
