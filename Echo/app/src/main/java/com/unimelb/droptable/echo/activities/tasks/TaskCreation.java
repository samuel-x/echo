package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;

public class TaskCreation extends AppCompatActivity {

    // Grab UI references
    private Button newTaskButton;
    private Button savedTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_start);

        // Setup our buttons
        newTaskButton = findViewById(R.id.buttonNewTask);
        savedTaskButton = findViewById(R.id.buttonSavedTasks);

        // Make the new task button functional
        newTaskButton.setOnClickListener((view) -> {newTask();});
    }

    /**
     * Starts the process of creating a new task.
     */
    private void newTask() {
        startActivity(new Intent(this, TaskCategories.class));
    }
}
