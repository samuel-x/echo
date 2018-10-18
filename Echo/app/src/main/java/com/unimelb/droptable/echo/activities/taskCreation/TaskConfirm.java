package com.unimelb.droptable.echo.activities.taskCreation;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.ApMapActivity;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;
import com.unimelb.droptable.echo.Utility;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;

/**
 * The Activity for Task Categories.
 * Allows the AP to select the correct category for their task
 */
public class TaskConfirm extends AppCompatActivity {

    // UI references
    /** The Title. */
    protected TextView title;
    /** The Address. */
    protected TextView address;
    /** The Notes. */
    protected TextView notes;
    /** The Payment amount. */
    protected TextView paymentAmount;
    /** The Confirm button. */
    protected Button confirmButton;
    /** The Task. */
    protected ImmutableTask task;

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
        confirmButton.setOnClickListener((view) -> {confirmSubmit();});
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

        // Go back to the map and end the activity.
        startActivity(new Intent(this, ApMapActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
