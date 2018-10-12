package com.unimelb.droptable.echo.activities.taskCreation;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

public class TaskConfirm extends AppCompatActivity {

    // Grab UI references
    protected TextView title;
    protected TextView address;
    protected TextView notes;
    protected Button confirmButton;
    protected ImmutableTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_confirm);

        task = Utility.currentTaskBuilder.status("PENDING").ap(ClientInfo.getUsername()).build();

        // Setup our buttons
        title = findViewById(R.id.textTaskConfirmTitle);
        address = findViewById(R.id.textTaskConfirmAddress);
        notes = findViewById(R.id.textTaskConfirmNotes);
        confirmButton = findViewById(R.id.buttonTaskConfirmConfirm);

        title.setText(task.getTitle());
        address.setText(task.getAddress());
        notes.setText(task.getNotes());

        confirmButton.setOnClickListener((view) -> {confirmSubmit();});
    }

    protected void confirmSubmit() {
        // Submit and remember the task.
        ClientInfo.setTask(task);
        FirebaseAdapter.pushTask(task);

        // End this activity.
        finish();
    }
}
