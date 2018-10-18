package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.Utility;

public class TaskCategories extends AppCompatActivity {

    // Grab UI references
    private Button categoryTransportButton;
    private Button categoryDeliveryButton;
    private Button categoryHouseholdButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_category);

        // Setup our buttons
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

    protected void householdTask() {
        Utility.currentTaskBuilder.category("Household");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Household"));
    }

    protected void deliveryTask() {
        Utility.currentTaskBuilder.category("Delivery");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Delivery"));
    }

    protected void transportTask() {
        Utility.currentTaskBuilder.category("Transport");
        startActivity(new Intent(this, TaskSubcategories.class).putExtra("category", "Transport"));
    }
}
