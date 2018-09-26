package com.unimelb.droptable.echo.activities;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;
import com.unimelb.droptable.echo.R;

import java.util.ArrayList;
import java.util.List;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String TAG = "so47492459";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
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
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // WORKSHOP LOCATION FOR TESTING : -37.800900, 144.958850

        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        //mMap.addMarker(new MarkerOptions().position(melbourne).title("Marker in Melbourne"));

        LatLng fitzroy = new LatLng(-37.8011,144.9789);
        //mMap.addMarker(new MarkerOptions().position(fitzroy).title("Marker in Fitzroy"));

        LatLng carlton = new LatLng( -37.8001, 144.9671);
        //mMap.addMarker(new MarkerOptions().position(carlton).title("Marker in Carlton"));

        LatLng southbank = new LatLng(-37.8290, 144.9570);
        mMap.addMarker(new MarkerOptions().position(southbank).title("Destination: Southbank"));

        LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        Location location;
        double longitude;
        double latitude;

        try {
            location = lm.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            LatLng UserLoc = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(UserLoc).title("Your Location"));

        } catch (SecurityException e) {
            Log.d("GPS_error", "Your GPS is not working");
            latitude = carlton.latitude;
            longitude = carlton.longitude;
        }

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();


        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyBqDcWb2dGBHZ_mOtFPRS7rJ72g2KnaIFk")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, ("\"" + latitude + "," + longitude + "\""), "-37.8011,144.9789");
        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
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
        } catch(Exception ex) {
            Log.e(TAG, ex.getLocalizedMessage());
        }

        //Draw the polyline
        if (path.size() > 0) {
            PolylineOptions opts = new PolylineOptions().addAll(path).color(Color.BLUE).width(5);
            mMap.addPolyline(opts);
        }

        mMap.getUiSettings().setZoomControlsEnabled(true);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 14));
    }
}
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.design.widget.FloatingActionButton;
//import android.support.v4.app.FragmentActivity;
//import android.widget.Button;
//
//import com.google.android.gms.maps.CameraUpdateFactory;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
//import com.unimelb.droptable.echo.activities.tasks.TaskCreation;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class MapActivity extends FragmentActivity implements OnMapReadyCallback {
//
//    private GoogleMap mMap;
//
//    private FloatingActionButton newTaskButton;
//
//    private FloatingActionButton settingsButton;
//    private FloatingActionButton infoButton;
//    private Button paymentButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_map);
//        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        newTaskButton = findViewById(R.id.addTaskButton);
//        newTaskButton.setOnClickListener((view) -> {newTask();});
//
//        // currently placeholders
//        settingsButton = findViewById(R.id.settingsButton);
//        infoButton = findViewById(R.id.infoButton);
//        paymentButton = findViewById(R.id.paymentButton);
//        paymentButton.setOnClickListener((view) -> {toPayment();});
//    }
//
//    private void newTask() {
//        startActivity(new Intent(this, TaskCreation.class));
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        // Add a marker in Sydney, Australia,
//        // and move the map's camera to the same location.
//        LatLng melbourne = new LatLng(-37.8136, 144.9631);
//
//        googleMap.addMarker(new MarkerOptions().position(melbourne)
//                .title("Marker in Melbourne"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(melbourne));
//
//        googleMap.setMinZoomPreference(12);
//
//    }
//
//    public void toPayment() {
//        startActivity(new Intent(this, PaymentActivity.class));
//    }
//
//}
