package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

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

        if (!getIntent().hasExtra("category")) {
            Log.d("Warning", "Missing key 'category'");
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

    private void taskDetailsA() {
        Utility.currentTaskBuilder.subCategory(subcategoryA.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
        finish();
    }

    private void taskDetailsB() {
        Utility.currentTaskBuilder.subCategory(subcategoryB.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
        finish();
    }
}
