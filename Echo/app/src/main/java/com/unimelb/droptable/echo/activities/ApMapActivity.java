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
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class ApMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

<<<<<<< HEAD
    private Button taskButton;
    private Button paymentDemo;

=======
    protected Button taskButton;
>>>>>>> bd878e0c2894f9b448c5f09c83e2aa4b687ed28c
    private FloatingActionButton helperButton;
    private FloatingActionButton accountButton;


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

<<<<<<< HEAD
        paymentDemo = findViewById(R.id.payment);
        paymentDemo.setOnClickListener(view -> {goToPayment();});

=======
        accountButton = findViewById(R.id.accountButtonAP);
        accountButton.setOnClickListener(view -> {onAccountButton();});
>>>>>>> bd878e0c2894f9b448c5f09c83e2aa4b687ed28c
    }

    private void goToPayment(){
        startActivity(new Intent(this, PaymentActivity.class));
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that the task button's text is up to date.
        if (ClientInfo.hasTask()) {

            // Attach chat listener.
            MessageNotification.AttachListener(ApMapActivity.this);

            // Attach Task listener.
            try {
                TaskNotification.AttachAPListener(ApMapActivity.this);
            } catch (TaskNotification.IncorrectListenerException e) {
                e.printStackTrace();
            }

            taskButton.setText(R.string.current_task_home_button);

            // If the task is completed, then we can proceed to the payment screen
            if (ClientInfo.getTask().getStatus().equals("COMPLETED")) {

                Context currentContext = this;

                // Show dialog
                TaskNotification.showDialog(this,
                        TaskNotification.TASK_COMPLETE_REQUEST_TITLE,
                        TaskNotification.TASK_COMPLETE_REQUEST_MESSAGE,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User touched the dialog's positive button
                                startActivity(new Intent(currentContext, PaymentActivity.class));
                            }
                        });
            }
        } else {
            taskButton.setText(R.string.new_task_home_button);
        }
    }

    protected void onAccountButton() {
        startActivity(new Intent(this, AccountActivity.class));
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


}