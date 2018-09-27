package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskAssistantList;
import com.unimelb.droptable.echo.activities.tasks.TaskCurrent;

public class AssistantMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    private Button taskButton;
    private FloatingActionButton settingsButton;
    private FloatingActionButton infoButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        // Get references to UI elements.

        taskButton = findViewById(R.id.assistantTaskButton);
        taskButton.setOnClickListener(view -> onTaskButtonClick());

        settingsButton = findViewById(R.id.settingsButton);
        infoButton = findViewById(R.id.infoButton);
        infoButton.setOnClickListener(view -> {
            startActivity(new Intent(this, ChatActivity.class)
                    .putExtra(getString(R.string.chat_partner), "TestChatPartner"));
        });
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
        startActivity(new Intent(this, TaskAssistantList.class));
    }
}
