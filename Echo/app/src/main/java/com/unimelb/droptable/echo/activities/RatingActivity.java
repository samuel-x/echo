package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseNotificationsAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class RatingActivity extends AppCompatActivity {

    private Button confirmRating;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        ratingBar = findViewById(R.id.ratingBar);
        confirmRating = findViewById(R.id.ratingButton);
        confirmRating.setOnClickListener((view)->{completeTask();});
    }

    private void completeTask() {
        ClientInfo.updateTask();
        ImmutableTask task = ClientInfo.getTask();
        try {
            FirebaseNotificationsAdapter.sendAssistantCompleteMessage(task);
        }
        catch (Exception e) {

        }
        FirebaseAdapter.updateUserRating(ClientInfo.getTask().getAssistant(), ratingBar.getRating());
        FirebaseAdapter.completeTask(ClientInfo.getTask());
        ClientInfo.setTask(null);

        startActivity(new Intent(this, ApMapActivity.class));
        finish();
    }
}
