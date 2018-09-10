package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;

public class TaskSubcategories extends AppCompatActivity {

    // Grab UI references
    private Button subcategoryA;
    private Button subcategoryB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_subcategory);

        // Setup our buttons
        subcategoryA = findViewById(R.id.buttonTaskSubcategoryA);
        subcategoryB = findViewById(R.id.buttonTaskSubcategoryB);

        subcategoryA.setOnClickListener((view) -> {taskDetails();});
        subcategoryB.setOnClickListener((view) -> {taskDetails();});
    }

    private void taskDetails() {
        startActivity(new Intent(this, TaskDetails.class));
    }

}
