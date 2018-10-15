package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
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
    //protected TextView address;
    protected PlaceAutocompleteFragment address;

    protected FloatingActionButton micInputButton;
    protected TextView taskNotes;
    protected ImageButton cameraButton;
    protected Button submitNowButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_details);

        // Setup our buttons
        title = findViewById(R.id.textTaskTitle);
        address = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.textTaskAddress);;
        micInputButton = findViewById(R.id.buttonTaskMic);
        taskNotes = findViewById(R.id.textTaskNotes);
        cameraButton = findViewById(R.id.buttonTaskCamera);
        submitNowButton = findViewById(R.id.buttonTaskNow);


        submitNowButton.setOnClickListener((view) -> {
            onContinue();});

        address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TEST:", "Place: " + place.getName());
                Utility.currentTaskBuilder.address(place.getName().toString());
                Utility.currentTaskBuilder.latitude(Double.valueOf(place.getLatLng().latitude).toString());
                Utility.currentTaskBuilder.longitude(Double.valueOf(place.getLatLng().longitude).toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TEST:", "An error occurred: " + status);
            }
        });

    }

    protected void onContinue() {
        Utility.currentTaskBuilder.title(title.getText().toString());
        //Utility.currentTaskBuilder.address(address.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        startActivity(new Intent(this, TaskConfirm.class));
        finish();
    }
}
