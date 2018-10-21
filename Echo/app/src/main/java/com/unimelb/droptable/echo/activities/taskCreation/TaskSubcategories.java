package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.Utility;

/**
 * The Task Subcategories Activity.
 * The activity to allow an AP to select one of two specific subcategories of their task.
 * This activity is shown after selecting a category in the TaskCategories Activity.
 */
public class TaskSubcategories extends AppCompatActivity {

    //UI references
    /** The first subcategory button */
    protected Button subcategoryA;
    /** The second subcategory button */
    protected Button subcategoryB;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_subcategory);

        // Setup our buttons
        subcategoryA = findViewById(R.id.buttonTaskSubcategoryA);
        subcategoryB = findViewById(R.id.buttonTaskSubcategoryB);

        //Determines what the category selected was and what it's subcategories are
        if (!getIntent().hasExtra("category")) {
            // If no Intent is given to the activity an error occurs
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

        // Subscribe to click events.
        subcategoryA.setOnClickListener((view) -> taskDetailsA());
        subcategoryB.setOnClickListener((view) -> taskDetailsB());
    }

    /**
     * First Subcategory Task. <p>
     * Called when the subcategoryA button is pressed. <p>
     * Enters the AP in Task Detail activity
     */
    protected void taskDetailsA() {
        //Stores the selected subcategory in the task
        Utility.currentTaskBuilder.subCategory(subcategoryA.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
    }

    /**
     * Second Subcategory Task. <p>
     * Called when the subcategoryB button is pressed. <p>
     * Enters the AP in Task Detail activity
     */
    protected void taskDetailsB() {
        //Stores the selected subcategory in the task
        Utility.currentTaskBuilder.subCategory(subcategoryB.getText().toString());
        startActivity(new Intent(this, TaskDetails.class));
    }
}
