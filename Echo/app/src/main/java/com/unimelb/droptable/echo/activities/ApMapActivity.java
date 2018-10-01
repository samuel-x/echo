package com.unimelb.droptable.echo.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.taskCreation.TaskCreation;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class ApMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button taskButton;
    private FloatingActionButton helperButton;

    private Button paymentButton;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;

    private Location currentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // setup our location provider
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Setup our location callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                else {
                    currentLocation = locationResult.getLastLocation();
                    Log.d("Lat:", String.valueOf(locationResult.getLastLocation().getLatitude()));
                    Log.d("Lon:", String.valueOf(locationResult.getLastLocation().getLongitude()));
                    try {
                        mMap.addMarker(new MarkerOptions().position(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude())).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                    catch (Exception e) {
                        Log.d("help:", "null marker");
                    }
                }
            };
        };

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references and set listeners.

        taskButton = findViewById(R.id.apTaskButton);
        taskButton.setOnClickListener((view) -> {onTaskPress();});

        helperButton = findViewById(R.id.apMapHelperButton);
        helperButton.setOnClickListener(view -> {
            onHelperPress();
        });

        paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener((view) -> {
            toPayment();
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        // Ensure that the task button's text is up to date.
        if (ClientInfo.hasTask()) {
            taskButton.setText(R.string.current_task_home_button);
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

    private void onTaskPress() {
        if (ClientInfo.hasTask()) {
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskCreation.class));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
        LatLng melbourne = new LatLng(-37.8136, 144.9631);

//        googleMap.addMarker(new MarkerOptions().position(melbourne)
//                .title("Marker in Melbourne"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));

        googleMap.setMinZoomPreference(12);
        startLocationUpdates();
    }

    private void onHelperPress() {
        HelperActivity.setCurrentHelperText(String.format(
                getString(R.string.home_help_text),
                getString(R.string.new_task_home_button),
                getString(R.string.current_task_home_button)
        ));
        startActivity(new Intent(this, HelperActivity.class));
    }

    public void toPayment() {
        startActivity(new Intent(this, PaymentActivity.class));
    }

}
