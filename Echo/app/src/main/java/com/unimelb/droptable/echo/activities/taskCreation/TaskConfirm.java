package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.Utility;
import com.unimelb.droptable.echo.activities.ApMapActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * The Activity for Task Categories.
 * Allows the AP to select the correct category for their task
 */
public class TaskConfirm extends AppCompatActivity {

    private static final String ERROR_MESSAGE = "Error attempting to show dialog. Please update" +
            "your android device.";
    // UI references
    protected TextView title;
    protected TextView address;
    protected TextView notes;
    protected TextView paymentAmount;
    protected Button confirmButton;
    protected ImmutableTask task;
    protected PlaceAutocompleteFragment homeAddress;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_confirm);

        // Get the current Task from currentTaskBuilder
        task = Utility.currentTaskBuilder.status("PENDING").ap(ClientInfo.getUsername())
                .lastPhase("false").build();

        // Setup the UI elements
        title = findViewById(R.id.textTaskConfirmTitle);
        address = findViewById(R.id.textTaskConfirmAddress);
        notes = findViewById(R.id.textTaskConfirmNotes);
        paymentAmount = findViewById(R.id.paymentAmount);
        confirmButton = findViewById(R.id.buttonTaskConfirmConfirm);

        //Subscribe to click events
        confirmButton.setOnClickListener((view) -> confirmSubmit());

    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Get the current Task from currentTaskBuilder
        task = Utility.currentTaskBuilder.status("PENDING").ap(ClientInfo.getUsername()).build();

        // Set the text of the UI elements
        title.setText(task.getTitle());
        address.setText(task.getAddress());
        notes.setText(task.getNotes());
        String amountString = task.getPaymentAmount();
        amountString = "Task Price: $"+amountString;
        paymentAmount.setText(amountString);
    }

    /**
     * Confirms that the current task selected. <p>
     * Sends the current task to the firebase database and returns the user to Map Activity.
     */
    protected void confirmSubmit() {
        // Submit and remember the task.
        ClientInfo.setTask(task);
        FirebaseAdapter.pushTask(task);

        // Check we have a home address, if we don't, ask for one.
        if (FirebaseAdapter.getHomeAddress(ClientInfo.getUsername()) == null) {
            android.app.AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new android.app.AlertDialog.Builder(this,
                        android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new android.app.AlertDialog.Builder(this);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder.setView(R.layout.home_address)
                        .setTitle("Set an Address")
                        .setMessage("Please set your home address below.")
                        .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        })
                        .show();
                homeAddress = (PlaceAutocompleteFragment)
                        getFragmentManager().findFragmentById(R.id.textHomeAddressModifyDialog);

                homeAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                    @Override
                    public void onPlaceSelected(Place place) {
                        // Assign their home.
                        FirebaseAdapter.updateHomeAddress(ClientInfo.getUsername(), place);
                        return;
                    }

                    @Override
                    public void onError(Status status) {
                        Toast.makeText(TaskConfirm.this,
                                status.getStatusMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }

        // Go back to the map and end the activity.
        startActivity(new Intent(this, ApMapActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
