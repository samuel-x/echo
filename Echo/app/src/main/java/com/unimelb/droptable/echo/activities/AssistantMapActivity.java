package com.unimelb.droptable.echo.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.Utility;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantList;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

import java.util.List;

public class AssistantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final int MS_LOCATION_UPDATE = 15000;
    private static final int MS_LOCATION_FAST_UPDATE = 10000;

    private float COMPLETION_DISTANCE = 50;

    private GoogleMap mMap;

    public Button taskButton;
    private FloatingActionButton accountButton;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;

    private Button completeTaskButton;

    private String fstInstruction;
    private String sndInstruction;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_map);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(MS_LOCATION_UPDATE);
        mLocationRequest.setFastestInterval(MS_LOCATION_FAST_UPDATE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Setup our location callback
        mLocationCallback = newCallBack();

        // Read from the database to see if the assistant already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references to UI elements.

        taskButton = findViewById(R.id.assistantTaskButton);
        taskButton.setOnClickListener(view -> onTaskButtonClick());

        completeTaskButton = findViewById(R.id.completeTaskButton);
        completeTaskButton.setOnClickListener(view -> onCompleteTaskButton());

        accountButton = findViewById((R.id.accountButtonAssistant));
        accountButton.setOnClickListener(view -> onAccountButton());

    }

    @Override
    protected void onResume() {
        super.onResume();

        // Read from the database to see if the assistant already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Ensure that the task button's text is up to date and update our listeners.
        if (ClientInfo.hasTask()) {

            // Attach Task Listener
            try {
                TaskNotification.AttachAssistantListener(AssistantMapActivity.this);
            } catch (TaskNotification.IncorrectListenerException e) {
                e.printStackTrace();
            }

            taskButton.setText(R.string.current_task_home_button);

            // Try to attach a chat listener.
            MessageNotification.AttachListener(AssistantMapActivity.this);
        } else {

            disableCompleteTask();
            taskButton.setText(R.string.new_task_home_button);
        }
    }

    private void onCompleteTaskButton() {

        if (!ClientInfo.getTask().getLastPhase().equals("true")){
            FirebaseAdapter.updatePhase();
            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("Task Update Sent")
                    .setMessage("Task Updated!")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();

            return;
        }

        ClientInfo.updateTask();
        FirebaseAdapter.updateTaskStatus("COMPLETED", ClientInfo.getTask().getId());
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Complete Request")
                .setMessage("Task Completion has been requested.")
                .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    protected void enableCompleteTask() {
        completeTaskButton.setEnabled(true);
        completeTaskButton.setAlpha(1.0f);
    }

    public void disableCompleteTask() {
        completeTaskButton.setEnabled(false);
        completeTaskButton.setAlpha(0.0f);
    }

    protected void onAccountButton() {
        startActivity(new Intent(this, AccountActivity.class));
    }

    @Override
    public void onMapReady(GoogleMap googleMap){

        mMap = googleMap;

        LatLng melbourne = new LatLng(-37.8136, 144.9631);

        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));

        googleMap.setMinZoomPreference(12);
        startLocationUpdates();

    }

    private void doMap(GoogleMap googleMap, LatLng startLL, LatLng endLL) {

        mMap = googleMap;

        if (ClientInfo.getTask().getLastPhase().equals("false")) {
            mMap.addMarker(new MarkerOptions().
                    position(endLL)
                    .title("Destination Pick Up")
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED)));
        }
        else if (ClientInfo.getTask().getLastPhase().equals("true")) {
            mMap.addMarker(new MarkerOptions().
                    position(endLL)
                    .title("Final Destination")
                    .icon(BitmapDescriptorFactory.defaultMarker(
                            BitmapDescriptorFactory.HUE_RED)));
        }

        // to center the map, need to have midpoint of start and end points
        LatLng midPoint = Utility.getMidPoint(startLL, endLL);

        // THIS ROUTE DRAWING BASED ON:
        // https://stackoverflow.com/questions/47492459/
        // android-google-maps-draw-a-route-between-two-points-along-the-road

        // Define list to get all latlng for the route
        List<LatLng> path = Utility.getPath(startLL, endLL);

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(8);
            mMap.addPolyline(opts);
        }

        // Move our camera
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(startLL));
    }

    protected void onTaskButtonClick() {
        if (ClientInfo.hasTask()) {
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskAssistantList.class));
        }
    }

    private boolean goHomeFirst(){

        if (ClientInfo.getTask().getCategory().equals("Transport")){

            fstInstruction = "COMPLETE PICK UP";
            sndInstruction = "COMPLETE DROP OFF";

            return ClientInfo.getTask().getSubCategory().equals("From My House");
        }

        if (ClientInfo.getTask().getCategory().equals("Delivery")){

            fstInstruction = "PICK UP ITEM(S)";
            sndInstruction = "DROP OFF ITEM(S)";

            return ClientInfo.getTask().getSubCategory().equals("From My House");
        }

        if (ClientInfo.getTask().getCategory().equals("Household")){

            fstInstruction = "ARRIVE AT HOUSE";
            sndInstruction = "COMPLETE TASK(S)";

            return true;
        }

        completeTaskButton.setText("COMPLETE TASK");

        return false;
    }

    private LatLng thirdStop(){

        // TODO: NEED TO HAVE USERS STORE THEIR HOME LAT LONGS / CURRENT LOCATIONS

        // placeholder is Arts West

        return new LatLng(-37.7976, 144.9594);
    }

    private LocationCallback newCallBack() {
        return new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    ClientInfo.setCurrentLocation(locationResult.getLastLocation());

                    try {
                        // Draw our marker and clear the map
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().
                                position(new LatLng(locationResult.getLastLocation().getLatitude(),
                                        locationResult.getLastLocation().getLongitude()))
                                .title("Your Location")
                                .icon(BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_AZURE)));

                        if (ClientInfo.hasTask()) {
                            checkStatus();
                            FirebaseAdapter.updateLocationOfUser(locationResult.getLastLocation());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
    }

    private void checkStatus() {
        ClientInfo.updateTask();

        LatLng startLL = ClientInfo.getCurrentLocationAsLatLng();

        LatLng endLL = new LatLng(
                Double.parseDouble(ClientInfo.getTask().getLatitude()),
                Double.parseDouble(ClientInfo.getTask().getLongitude()));

        LatLng midStop = thirdStop();
        boolean goHome = goHomeFirst();

        if (!goHome) {
            LatLng temp = endLL;
            endLL = midStop;
            midStop = temp;
        }

        if (ClientInfo.getTask().getLastPhase().equals("false")) {
            doMap(mMap, startLL, midStop);
            if (Utility.distance(startLL, midStop) < COMPLETION_DISTANCE) {
                completeTaskButton.setText(fstInstruction);
                enableCompleteTask();
            } // Note: Else was disabled for the demo
            else {
                disableCompleteTask();
            }
        } else {
            doMap(mMap, startLL, endLL);
            if (Utility.distance(startLL, endLL) < COMPLETION_DISTANCE) {
                completeTaskButton.setText(sndInstruction);
                enableCompleteTask();
            } // Note: Else was disabled for the demo
            else {
                disableCompleteTask();
            }
        }
    }
}
