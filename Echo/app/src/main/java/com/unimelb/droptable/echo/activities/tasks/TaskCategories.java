package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;

public class TaskCategories extends AppCompatActivity {

    // Grab UI references
    private Button categoryTransportButton;
    private Button categoryDeliveryButton;
    private Button categoryHouseholdButton;
    private Button categoryOtherButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_category);

        // Setup our buttons
        categoryTransportButton = findViewById(R.id.buttonTaskTransport);
        categoryDeliveryButton = findViewById(R.id.buttonTaskDelivery);
        categoryHouseholdButton = findViewById(R.id.buttonTaskHousehold);
        categoryOtherButton = findViewById(R.id.buttonTaskOther);

        categoryTransportButton.setOnClickListener((view) -> {transportTask();});
        categoryDeliveryButton.setOnClickListener((view) -> {deliveryTask();});
        categoryHouseholdButton.setOnClickListener((view) -> {householdTask();});
        categoryOtherButton.setOnClickListener((view) -> {otherTask();});
    }

    private void otherTask() {
        startActivity(new Intent(this, TaskDetails.class));
    }

    private void householdTask() {
        startActivity(new Intent(this, TaskSubcategories.class));
    }

    private void deliveryTask() {
        startActivity(new Intent(this, TaskSubcategories.class));
    }

    private void transportTask() {
        startActivity(new Intent(this, TaskSubcategories.class));
    }

}
