package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.Task;

public class TaskSubcategories extends AppCompatActivity {

    // Grab UI references
    private Button subcategoryA;
    private Button subcategoryB;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_subcategory);

        task = (Task) getIntent().getSerializableExtra("task");

        // Setup our buttons
        subcategoryA = findViewById(R.id.buttonTaskSubcategoryA);
        subcategoryB = findViewById(R.id.buttonTaskSubcategoryB);

        if (task.getCategory().equals("Household")) {
            subcategoryA.setText("Cooking");
            subcategoryB.setText("Cleaning");
        }
        else if (task.getCategory().equals("Transport")) {
            subcategoryA.setText("From My House");
            subcategoryB.setText("To My House");
        }
        else if (task.getCategory().equals("Delivery")) {
            subcategoryA.setText("From My House");
            subcategoryB.setText("To My House");
        }

        subcategoryA.setOnClickListener((view) -> {taskDetailsA();});
        subcategoryB.setOnClickListener((view) -> {taskDetailsB();});
    }

    private void taskDetailsB() {
        task.setSubcategory("B");
        startActivity(new Intent(this, TaskDetails.class).putExtra("task", task));
    }

    private void taskDetailsA() {
        task.setSubcategory("A");
        startActivity(new Intent(this, TaskDetails.class).putExtra("task", task));
    }
}
