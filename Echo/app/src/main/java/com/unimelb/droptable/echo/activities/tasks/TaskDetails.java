package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;

public class TaskDetails extends AppCompatActivity {

    // Grab UI references
    private TextView title;
    private TextView address;
    private FloatingActionButton micInputButton;
    private TextView taskNotes;
    private ImageButton cameraButton;
    private Button submitNowButton;
    private Button submitLaterButton;

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

    private void submitNow() {
        startActivity(new Intent(this, TaskConfirm.class));
    }


}
