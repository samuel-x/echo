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

public class TaskDetails extends AppCompatActivity {
    // Grab UI references
    protected TextView title;
    protected PlaceAutocompleteFragment address;
    protected TextView taskNotes;
    protected Button submitNowButton;
    protected TextView paymentAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_details);

        // Setup our buttons
        title = findViewById(R.id.textTaskTitle);
        address = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.textTaskAddress);;
        taskNotes = findViewById(R.id.textTaskNotes);
        submitNowButton = findViewById(R.id.buttonTaskNow);
        paymentAmount = findViewById(R.id.paymentAmount);


        submitNowButton.setOnClickListener((view) -> {
            onContinue();});

        address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Utility.currentTaskBuilder.address(place.getName().toString());
                Utility.currentTaskBuilder.latitude(Double.valueOf(place.getLatLng().latitude).toString());
                Utility.currentTaskBuilder.longitude(Double.valueOf(place.getLatLng().longitude).toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
            }
        });

    }

    protected void onContinue() {
        Utility.currentTaskBuilder.title(title.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        Utility.currentTaskBuilder.paymentAmount(paymentAmount.getText().toString());
        startActivity(new Intent(this, TaskConfirm.class));
        finish();
    }
}
