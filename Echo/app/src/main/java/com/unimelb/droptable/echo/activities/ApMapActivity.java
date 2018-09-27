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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ap_map);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Read from the database to see if the AP already has a task in progress.
        ClientInfo.setTask(FirebaseAdapter.getCurrentTask());

        // Get references and set listeners.

        taskButton = findViewById(R.id.taskButton);
        taskButton.setOnClickListener((view) -> {onTaskPress();});
        if (ClientInfo.hasTask()) {
            taskButton.setText(R.string.current_task_home_button);
        } else {
            taskButton.setText(R.string.new_task_home_button);
        }


        helperButton = findViewById(R.id.apMapHelperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});

        paymentButton = findViewById(R.id.paymentButton);
        paymentButton.setOnClickListener((view) -> {toPayment();});
    }

    private void onTaskPress() {
        if (ClientInfo.hasTask()) {
            // TODO: This is a placeholder. Needs to go to task details screen, not login.
            startActivity(new Intent(this, TaskCurrent.class));
        } else {
            startActivity(new Intent(this, TaskCreation.class));
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

    public void toPayment() {
        startActivity(new Intent(this, PaymentActivity.class));
    }

}
