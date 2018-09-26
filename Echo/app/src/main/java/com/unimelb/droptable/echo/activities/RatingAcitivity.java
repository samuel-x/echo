package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;

public class RatingAcitivity extends AppCompatActivity {

    private Button confirmRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        confirmRating = findViewById(R.id.ratingButton);
        confirmRating.setOnClickListener((view)->{goToMap();});
    }

    private void goToMap() {
        startActivity(new Intent(this, ApMapActivity.class));
        finish();
    }
}
