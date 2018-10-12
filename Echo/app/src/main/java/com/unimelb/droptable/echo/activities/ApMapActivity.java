package com.unimelb.droptable.echo.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.taskCreation.TaskCategories;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class ApMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    protected Button taskButton;
    private FloatingActionButton helperButton;
    private FloatingActionButton accountButton;

    private Query taskQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        taskButton = findViewById(R.id.apTaskButton);
        taskButton.setOnClickListener((view) -> {onTaskPress();});

        helperButton = findViewById(R.id.apMapHelperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});

        accountButton = findViewById(R.id.accountButtonAP);
        accountButton.setOnClickListener(view -> {onAccountButton();});
    }

    protected void onAccountButton() {
        startActivity(new Intent(this, AccountActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that the task button's text is up to date.
        if (ClientInfo.hasTask()) {

            // Attach task listener.
            while (taskQuery == null) {
                try {
                    taskQuery = FirebaseAdapter.queryCurrentTask();
                    taskQuery.addChildEventListener(createTaskListener());
                } catch (NullPointerException e) {
                    taskQuery = null;
                }
            }

            taskButton.setText(R.string.current_task_home_button);

            // If the task is completed, then we can proceed to the payment screen
            if (ClientInfo.getTask().getStatus().equals("COMPLETED")) {

                Context currentContext = this;

                // Show dialog
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this,
                            android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Complete Task Request")
                        .setMessage("Task Completion has been requested by "
                                + ClientInfo.getTask().getAssistant())
                        .setPositiveButton(android.R.string.yes,
                                new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User touched the dialog's positive button
                                startActivity(new Intent(currentContext, PaymentActivity.class));
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        } else {
            taskButton.setText(R.string.new_task_home_button);
        }
    }

    protected void onTaskPress() {
        if (ClientInfo.hasTask()) {
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskCategories.class));
        }
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

    private void onHelperPress() {
        HelperActivity.setCurrentHelperText(String.format(
                getString(R.string.home_help_text),
                getString(R.string.new_task_home_button),
                getString(R.string.current_task_home_button)
        ));
        startActivity(new Intent(this, HelperActivity.class));
    }

    protected ChildEventListener createTaskListener() {
        return new ChildEventListener() {

            // Check that the added value is an assistant and show the dialog
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot != null &&
                        dataSnapshot.getValue(String.class) != null &&
                        dataSnapshot.getKey().toString().equals("assistant")) {
                    String assistantID = dataSnapshot.getValue(String.class);
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ApMapActivity.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ApMapActivity.this);
                    }
                    builder.setTitle("Task Accepted!")
                            .setMessage("Your task has been accepted by " + assistantID + "!")
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();

                    // Update task with assistant.
                    ClientInfo.updateAssistant(assistantID);

                    // Attach chat listener.
                    MessageNotification.AttachListener(ApMapActivity.this);
                }
            }

            // Check that the task has been completed.
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot != null &&
                        dataSnapshot.getValue(String.class) != null &&
                        dataSnapshot.getKey().toString().equals("status") &&
                        dataSnapshot.getValue(String.class).equals("COMPLETED")) {

                    Context currentContext = ApMapActivity.this;

                    // Ensure that the activity is running.
                    if (!((Activity) currentContext).hasWindowFocus()) {
                        return;
                    }

                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(currentContext,
                                android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(currentContext);
                    }
                    builder.setTitle("Complete Task Request")
                            .setMessage("Task Completion has been requested by " +
                                    ClientInfo.getTask().getAssistant())
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // User touched the dialog's positive button
                                    startActivity(new Intent(currentContext,
                                            PaymentActivity.class));
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            }

            // If a task has been removed, show an error.
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().toString().equals("title")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(ApMapActivity.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(ApMapActivity.this);
                    }
                    builder.setTitle("Task Cancellation")
                            .setMessage("Unfortunately, the task has been cancelled.")
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    ClientInfo.setTask(null);
                    taskButton.setText(R.string.new_task_home_button);
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        };
    }
}