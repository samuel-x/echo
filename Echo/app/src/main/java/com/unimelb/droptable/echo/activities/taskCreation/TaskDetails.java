package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.Utility;

/**
 * The Task Details activity.
 * This activity is for an AP to fill out the specific details of their task
 */
public class TaskDetails extends AppCompatActivity {

    // UI references
    protected TextView title;
    protected PlaceAutocompleteFragment address;
    protected TextView taskNotes;
    protected Button submitNowButton;
    protected TextView paymentAmount;


    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_details);

        // Setup our buttons
        title = findViewById(R.id.textTaskTitle);
        address = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.textTaskAddress);
        taskNotes = findViewById(R.id.textTaskNotes);
        submitNowButton = findViewById(R.id.buttonTaskNow);
        paymentAmount = findViewById(R.id.paymentAmount);

        // Subscribe to click events.
        submitNowButton.setOnClickListener((view) -> onContinue());

        //Set up and implement the auto-fill feature for the address bar
        address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Utility.currentTaskBuilder.address(place.getName().toString());
                Utility.currentTaskBuilder.latitude(Double.valueOf(place.getLatLng().latitude).toString());
                Utility.currentTaskBuilder.longitude(Double.valueOf(place.getLatLng().longitude).toString());
            }

            @Override
            //This is called when there is an error with the auto-fill
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

    }

    /**
     * On continue. <p>
     * Called when submitNowButton is pressed. <p>
     * Builds a task from the entered details and continues to the TaskConfirm Activity.
     */
    protected void onContinue() {
        //Store the entered information
        Utility.currentTaskBuilder.title(title.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        Utility.currentTaskBuilder.paymentAmount(paymentAmount.getText().toString());

        //Start the new Activity and close this one
        startActivity(new Intent(this, TaskConfirm.class));
        finish();
    }
}
