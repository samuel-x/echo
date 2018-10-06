package com.unimelb.droptable.echo.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantList;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class AssistantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button taskButton;
    private Button completeTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Read from the database to see if the assistant already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references to UI elements.

        taskButton = findViewById(R.id.assistantTaskButton);
        taskButton.setOnClickListener(view -> onTaskButtonClick());

        completeTaskButton = findViewById(R.id.completeTaskButton);
        completeTaskButton.setOnClickListener(view -> onCompleteTaskButton());
    }

    private void onCompleteTaskButton() {
        FirebaseAdapter.updateTaskStatus("COMPLETED", ClientInfo.getTask().getId());
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Complete Request")
                .setMessage("Task Completion has been requested.")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that the task button's text is up to date.
        if (ClientInfo.hasTask()) {
            enableCompleteTask();
            taskButton.setText(R.string.current_task_home_button);
        } else {
            disableCompleteTask();
            taskButton.setText(R.string.new_task_home_button);
        }
    }

    private void enableCompleteTask() {
        completeTaskButton.setEnabled(true);
        completeTaskButton.setAlpha(1.0f);
    }

    private void disableCompleteTask() {
        completeTaskButton.setEnabled(false);
        completeTaskButton.setAlpha(0.0f);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        LatLng melbourne = new LatLng(-37.8136, 144.9631);

        googleMap.addMarker(new MarkerOptions().position(melbourne)
                .title("Marker in Melbourne"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));

        googleMap.setMinZoomPreference(12);
    }

    private void onTaskButtonClick() {
        if (ClientInfo.hasTask()) {
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskAssistantList.class));
        }
    }
}
