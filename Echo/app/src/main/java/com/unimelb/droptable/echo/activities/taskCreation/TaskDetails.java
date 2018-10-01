package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

public class TaskDetails extends AppCompatActivity {

    // Grab UI references
    private TextView title;
    private PlaceAutocompleteFragment address;
    private FloatingActionButton micInputButton;
    private TextView taskNotes;
    private ImageButton cameraButton;
    private Button submitNowButton;
    private Button submitLaterButton;

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
        submitLaterButton = findViewById(R.id.buttonTaskLater);


        submitNowButton.setOnClickListener((view) -> {submitNow();});
        submitLaterButton.setOnClickListener((view) -> {submitNow();});

        address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                Log.i("TEST:", "Place: " + place.getName());
                Utility.currentTaskBuilder.address(place.getName().toString());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("TEST:", "An error occurred: " + status);
            }
        });

    }

    private void submitNow() {
        Utility.currentTaskBuilder.title(title.getText().toString());
        Utility.currentTaskBuilder.notes(taskNotes.getText().toString());
        startActivity(new Intent(this, TaskConfirm.class));
        finish();
    }
}
