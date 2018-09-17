package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.unimelb.droptable.echo.R;

import com.unimelb.droptable.echo.activities.tasks.TaskCreation;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FloatingActionButton newTaskButton;

    private FloatingActionButton settingsButton;
    private FloatingActionButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        newTaskButton = findViewById(R.id.addTaskButton);
        newTaskButton.setOnClickListener((view) -> {newTask();});

        // currently placeholders
        settingsButton = findViewById(R.id.settingsButton);
        infoButton = findViewById(R.id.infoButton);
    }

    private void newTask() {
        startActivity(new Intent(this, TaskCreation.class));
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

}
