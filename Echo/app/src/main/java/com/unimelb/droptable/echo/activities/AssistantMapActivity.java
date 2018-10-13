package com.unimelb.droptable.echo.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.Api;
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
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantList;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import java.util.ArrayList;
import java.util.List;

//import com.unimelb.droptable.echo.activities.tasks.TaskCreation;

public class AssistantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "so47492459";

    private Button taskButton;
    private FloatingActionButton settingsButton;
    private FloatingActionButton infoButton;
    private Button paymentButton;
    private TextView todoText;

//    static Thread thread;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback mLocationCallback;

    private LocationRequest mLocationRequest;

    private Location currentLocation;

    private Button completeTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_map);
        //MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        //mapFragment.getMapAsync(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

//        newTaskButton = findViewById(R.id.addTaskButton);
//        newTaskButton.setOnClickListener((view) -> {
//            newTask();
//        });

        todoText = findViewById(R.id.todoText);
        todoText.setText("");

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
                    double startLat = locationResult.getLastLocation().getLatitude();
                    double startLon = locationResult.getLastLocation().getLongitude();
                    Log.d("Lat:", String.valueOf(startLat));
                    Log.d("Lon:", String.valueOf(startLon));

                    LatLng startLL = new LatLng(startLat, startLon);
                    LatLng southbank = new LatLng(-37.8290, 144.9570);
                    LatLng endLL = southbank;

                    try {
                        mMap.clear();
                        mMap.addMarker(new MarkerOptions().position(new LatLng(locationResult.getLastLocation().getLatitude(), locationResult.getLastLocation().getLongitude())).title("Your Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        if (ClientInfo.hasTask()) {

                            endLL = new LatLng(Double.parseDouble(ClientInfo.getTask().getLatitude()), Double.parseDouble(ClientInfo.getTask().getLongitude()));
                            Log.d("dest loc from database:", endLL.toString());
                            doMap(mMap, startLL, endLL);
                            //doMap(mMap, endLL, new LatLng(-37.8170, 144.9460));

                            todoText.setText("Go to " + ClientInfo.getTask().getAddress());
                           //todoText.setAllCaps(true);

                        }
                    }
                    catch (Exception e) {
                        Log.d("help:", "null marker");
                    }
                }
            };
        };


        // Read from the database to see if the assistant already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references to UI elements.

        taskButton = findViewById(R.id.assistantTaskButton);
        taskButton.setOnClickListener(view -> onTaskButtonClick());

        //settingsButton = findViewById(R.id.settingsButton);
        //infoButton = findViewById(R.id.infoButton);

        completeTaskButton = findViewById(R.id.completeTaskButton);
        completeTaskButton.setOnClickListener(view -> onCompleteTaskButton());


    }

    private void onCompleteTaskButton() {

        if (!ClientInfo.getTask().getLastPhase().equals("true")){
            FirebaseAdapter.updatePhase(ClientInfo.getUsername());
            ClientInfo.setTask(ImmutableTask.builder().from(ClientInfo.getTask()).lastPhase("true").build());
            return;
        }

        ClientInfo.updateTask();
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
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Ensure that the task button's text is up to date and update our listeners.
        if (ClientInfo.hasTask()) {
            ClientInfo.updateTask();
            ChildEventListener childEventListener = createListener();
            Query query = FirebaseAdapter.queryCurrentTask();
            query.addChildEventListener(childEventListener);
            enableCompleteTask();
            taskButton.setText(R.string.current_task_home_button);

            // Try to attach a chat listener.
            MessageNotification.AttachListener(AssistantMapActivity.this);
        } else {

            disableCompleteTask();
            taskButton.setText(R.string.new_task_home_button);
        }
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {

        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                mLocationCallback,
                null /* Looper */);
    }

    private void enableCompleteTask() {
        completeTaskButton.setEnabled(true);
        completeTaskButton.setAlpha(1.0f);
    }

    private void disableCompleteTask() {
        completeTaskButton.setEnabled(false);
        completeTaskButton.setAlpha(0.0f);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
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


        //LOCATIONS FOR TESTING

        // WORKSHOP LOCATION FOR TESTING: -37.800900, 144.958847

        LatLng melbourne = new LatLng(-37.8136, 144.9631);

        LatLng fitzroy = new LatLng(-37.8011, 144.9789);

        LatLng carlton = new LatLng(-37.8001, 144.9671);

        LatLng docklands = new LatLng(-37.8170, 144.9460);

        LatLng southbank = new LatLng(-37.8290, 144.9570);

        LatLng destination = endLL;

        mMap.addMarker(new MarkerOptions().position(destination).title("Destination: " + "Southbank"));

        //LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location;
        double longitude;
        double latitude;

        try {
            latitude = startLL.latitude;
            longitude = startLL.longitude;
            //mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title("Start Location").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        } catch (SecurityException e) {
            Log.d("GPS_error", "Your GPS is not working");
            latitude = carlton.latitude;
            longitude = carlton.longitude;
            mMap.addMarker(new MarkerOptions().position(carlton).title("Default Location: Carlton"));
        }

        // to center the map, need to have midpoint of start and end points
        LatLng midPoint = new LatLng ((latitude + destination.latitude)/2, (longitude + destination.longitude)/2);

        // THIS ROUTE DRAWING BASED ON:
        // https://stackoverflow.com/questions/47492459/android-google-maps-draw-a-route-between-two-points-along-the-road

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder().apiKey("AIzaSyBrPt88vvoPDDn_imh-RzCXl5Ha2F2LYig").build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context,
                ("\"" + latitude + "," + longitude + "\""), ("\"" + destination.latitude + "," + destination.longitude + "\""));
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs != null) {
                    for (int i = 0; i < route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j = 0; j < leg.steps.length; j++) {
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length > 0) {
                                    for (int k = 0; k < step.steps.length; k++) {
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(8);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(midPoint, 14));
    }



    private void onTaskButtonClick() {
        if (ClientInfo.hasTask()) {
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskAssistantList.class));
        }
    }

    /**
     * Create a listener to listen to changes on the task. If a task is removed, then show the
     * necessary dialog.
     * @return
     */
     private ChildEventListener createListener() {
        return new ChildEventListener() {

            // TODO: Implement these properly
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                ClientInfo.updateTask();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                ClientInfo.updateTask();
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getKey().toString().equals("title")) {
                    AlertDialog.Builder builder;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        builder = new AlertDialog.Builder(AssistantMapActivity.this,
                                android.R.style.Theme_Material_Dialog_Alert);
                    } else {
                        builder = new AlertDialog.Builder(AssistantMapActivity.this);
                    }
                    builder.setTitle("Task Complete")
                            .setMessage("The task was accepted by the AP!")
                            .setPositiveButton(android.R.string.yes,
                                    new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    ClientInfo.setTask(null);
                    disableCompleteTask();
                    taskButton.setText(R.string.new_task_home_button);
                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        };
    }
}