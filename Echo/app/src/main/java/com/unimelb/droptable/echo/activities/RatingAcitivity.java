package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class RatingAcitivity extends AppCompatActivity {

    private Button confirmRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        confirmRating = findViewById(R.id.ratingButton);
        confirmRating.setOnClickListener((view)->{completeTask();});
    }

    private void completeTask() {
        FirebaseAdapter.completeTask(ClientInfo.getTask().getId());
        startActivity(new Intent(this, ApMapActivity.class));
        finish();
    }
}
