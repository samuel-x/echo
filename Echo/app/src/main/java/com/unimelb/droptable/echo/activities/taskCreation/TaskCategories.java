package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.Utility;

/**
 * The Activity for Task Categories.
 * Allows the AP to select the correct category for their task.
 */
public class TaskCategories extends AppCompatActivity {

    //UI references
    protected Button categoryTransportButton;
    protected Button categoryDeliveryButton;
    protected Button categoryHouseholdButton;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_category);

        // Setup the category buttons
        categoryTransportButton = findViewById(R.id.buttonTaskTransport);
        categoryDeliveryButton = findViewById(R.id.buttonTaskDelivery);
        categoryHouseholdButton = findViewById(R.id.buttonTaskHousehold);

        // Subscribe to click events.
        categoryTransportButton.setOnClickListener((view) -> transportTask());
        categoryDeliveryButton.setOnClickListener((view) -> deliveryTask());
        categoryHouseholdButton.setOnClickListener((view) -> householdTask());

        // Begin a new taskBuilder object.
        Utility.currentTaskBuilder = ImmutableTask.builder();
    }

    /**
     * Household task. <p>
     * Called when categoryHouseholdButton is pressed. <p>
     * Enters the subcategory selection activity for Household tasks.
     */
    protected void householdTask() {
        Utility.currentTaskBuilder.category("Household");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Household"));
    }

    /**
     * Delivery task. <p>
     * Called when categoryDeliveryButton is pressed. <p>
     * Enters the subcategory selection activity for Delivery tasks.
     */
    protected void deliveryTask() {
        Utility.currentTaskBuilder.category("Delivery");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Delivery"));
    }

    /**
     * Transport tasks. <p>
     * Called when categoryDeliveryButton is pressed <p>
     * Enters the subcategory selection activity for Delivery tasks.
     */
    protected void transportTask() {
        Utility.currentTaskBuilder.category("Transport");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Transport"));
    }
}
