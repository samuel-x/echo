package com.unimelb.droptable.echo.activities.tasks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.HomePlaceholder;

public class TaskConfirm extends AppCompatActivity {


    // Grab UI references
    private TextView title;
    private TextView address;
    private TextView time;
    private Button setRecurringButton;
    private Button setRecentButton;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_creation_confirm);

        // Setup our buttons
        title = findViewById(R.id.textTaskConfirmTitle);
        address = findViewById(R.id.textTaskConfirmAddress);
        time = findViewById(R.id.textTaskConfirmTime);
        setRecentButton = findViewById(R.id.buttonTaskConfirmRecent);
        setRecurringButton = findViewById(R.id.buttonTaskConfirmRecurring);
        confirmButton = findViewById(R.id.buttonTaskConfirmConfirm);

        setRecentButton.setOnClickListener((view) -> {setRecent();});
        setRecurringButton.setOnClickListener((view) -> {setRecurring();});
        confirmButton.setOnClickListener((view) -> {confirmSubmit();});

    }

    private void setRecurring() {
        Log.d("test","recurring");
    }

    private void setRecent() {
        Log.d("test","recent");
    }
    private void confirmSubmit() {
        startActivity(new Intent(null, HomePlaceholder.class));

    }

}
