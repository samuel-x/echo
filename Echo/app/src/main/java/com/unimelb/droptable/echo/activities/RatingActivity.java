package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
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
        confirmRating.setOnClickListener((view)-> onConfirmRating());
    }

    protected void onConfirmRating() {
        completeTask();
    }

    protected void completeTask() {
        ClientInfo.updateTask();
        ImmutableTask task = ClientInfo.getTask();

        FirebaseAdapter.updateUserRating(task.getAssistant(), ratingBar.getRating());
        FirebaseAdapter.completeTask(task);

        ClientInfo.setTask(null);

        startActivity(new Intent(this, ApMapActivity.class));
        finish();
    }
}
