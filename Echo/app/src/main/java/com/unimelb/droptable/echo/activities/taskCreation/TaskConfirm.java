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

public class TaskConfirm extends AppCompatActivity {

    // Grab UI references
    protected TextView title;
    protected TextView address;
    protected TextView notes;
    protected TextView paymentAmount;
    protected Button confirmButton;
    protected ImmutableTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_confirm);

        task = Utility.currentTaskBuilder.status("PENDING").ap(ClientInfo.getUsername())
                .lastPhase("false").build();

        // Setup our buttons
        title = findViewById(R.id.textTaskConfirmTitle);
        address = findViewById(R.id.textTaskConfirmAddress);
        notes = findViewById(R.id.textTaskConfirmNotes);
        paymentAmount = findViewById(R.id.paymentAmount);
        confirmButton = findViewById(R.id.buttonTaskConfirmConfirm);
        confirmButton.setOnClickListener((view) -> {confirmSubmit();});
    }

    @Override
    protected void onResume() {
        super.onResume();

        task = Utility.currentTaskBuilder.status("PENDING").ap(ClientInfo.getUsername()).build();
        title.setText(task.getTitle());
        address.setText(task.getAddress());
        notes.setText(task.getNotes());
        String amountString = task.getPaymentAmount();
        amountString = "Task Price: $"+amountString;
        paymentAmount.setText(amountString);
    }

    protected void confirmSubmit() {
        // Submit and remember the task.
        ClientInfo.setTask(task);
        FirebaseAdapter.pushTask(task);

        // Go back to the map and end the activity.
        startActivity(new Intent(this, ApMapActivity.class).addFlags(FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}
