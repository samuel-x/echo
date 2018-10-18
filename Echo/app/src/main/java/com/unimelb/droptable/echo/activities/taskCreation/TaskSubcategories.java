package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.Utility;

public class TaskSubcategories extends AppCompatActivity {
    // Grab UI references
    protected Button subcategoryA;
    protected Button subcategoryB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_subcategory);

        // Setup our buttons
        subcategoryA = findViewById(R.id.buttonTaskSubcategoryA);
        subcategoryB = findViewById(R.id.buttonTaskSubcategoryB);

        if (!getIntent().hasExtra("category")) {
            throw new RuntimeException("Missing key 'category'");
        }
        else if (getIntent().getExtras().getString("category").equals("Household")) {
            subcategoryA.setText("Cooking");
            subcategoryB.setText("Cleaning");
        }
        else if (getIntent().getExtras().getString("category").equals("Transport")) {
            subcategoryA.setText("From My House");
            subcategoryB.setText("To My House");
        }
        else if (getIntent().getExtras().getString("category").equals("Delivery")) {
            subcategoryA.setText("From My House");
            subcategoryB.setText("To My House");
        }

        subcategoryA.setOnClickListener((view) -> {taskDetailsA();});
        subcategoryB.setOnClickListener((view) -> {taskDetailsB();});
    }

    protected void taskDetailsA() {
        Utility.currentTaskBuilder.subCategory(subcategoryA.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
    }

    protected void taskDetailsB() {
        Utility.currentTaskBuilder.subCategory(subcategoryB.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
    }
}
