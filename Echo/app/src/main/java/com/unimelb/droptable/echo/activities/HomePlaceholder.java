package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.unimelb.droptable.echo.R;

import android.support.v7.widget.Toolbar;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.unimelb.droptable.echo.activities.tasks.TaskCreation;

public class HomePlaceholder extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private FloatingActionButton newTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_placeholder);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        newTaskButton = findViewById(R.id.add_task_button);
        newTaskButton.setOnClickListener((view) -> {newTask();});
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
    }

}
