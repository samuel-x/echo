package com.unimelb.droptable.echo.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.taskCreation.TaskCategories;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class ApMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;



    private Button paymentDemo;


    protected Button taskButton;

    private FloatingActionButton helperButton;
    private FloatingActionButton accountButton;


    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;
    private PlaceAutocompleteFragment address;

    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setup our location provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(1000);
        mLocationRequest.setFastestInterval(500);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Setup our location callback
        mLocationCallback = createNewAPCallback();

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references and set listeners.

        taskButton = findViewById(R.id.apTaskButton);
        taskButton.setOnClickListener((view) -> {onTaskPress();});

        helperButton = findViewById(R.id.apMapHelperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});


        paymentDemo = findViewById(R.id.payment);
        paymentDemo.setOnClickListener(view -> {goToPayment();});


        accountButton = findViewById(R.id.accountButtonAP);
        accountButton.setOnClickListener(view -> {onAccountButton();});

    }

    private void goToPayment(){
        startActivity(new Intent(this, PaymentActivity.class));
    }



    @Override
    protected void onResume() {
        super.onResume();

        // Check Firebase for an existing task.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

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

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));

        googleMap.setMinZoomPreference(12);
        startLocationUpdates();
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
    private void onHelperPress() {
        HelperActivity.setCurrentHelperText(String.format(
                getString(R.string.home_help_text),
                getString(R.string.new_task_home_button),
                getString(R.string.current_task_home_button)
        ));
        startActivity(new Intent(this, HelperActivity.class));
    }

    private LocationCallback createNewAPCallback() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                else {
                    ClientInfo.setCurrentLocation(locationResult.getLastLocation());
                    try {
                        if (currentLocation != null){
                            mMap.clear();
                            mMap.addMarker(new MarkerOptions().position(new
                                    LatLng(locationResult.getLastLocation().getLatitude(),
                                    locationResult.getLastLocation().getLongitude()))
                                    .title("Your Location")
                                    .icon(BitmapDescriptorFactory.defaultMarker(
                                            BitmapDescriptorFactory.HUE_AZURE)));
                        }
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

}