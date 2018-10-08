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
    protected FloatingActionButton micInputButton;
    protected TextView taskNotes;
    protected ImageButton cameraButton;
    protected Button submitNowButton;
    protected Button submitLaterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_details);

        // Setup our buttons
        title = findViewById(R.id.textTaskTitle);
        address = findViewById(R.id.textTaskAddress);
        micInputButton = findViewById(R.id.buttonTaskMic);
        taskNotes = findViewById(R.id.textTaskNotes);
        cameraButton = findViewById(R.id.buttonTaskCamera);
        submitNowButton = findViewById(R.id.buttonTaskNow);
        submitLaterButton = findViewById(R.id.buttonTaskLater);

        submitNowButton.setOnClickListener((view) -> {submitNow();});
        submitLaterButton.setOnClickListener((view) -> {submitNow();});

    }

    protected void submitNow() {
        Utility.currentTaskBuilder.title(title.getText().toString());
        Utility.currentTaskBuilder.address(address.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        startActivity(new Intent(this, TaskConfirm.class));
        finish();
    }
}
