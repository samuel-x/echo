package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.unimelb.droptable.echo.R;

public class PaymentActivity extends AppCompatActivity {

    private Button confirmPayment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        confirmPayment = findViewById(R.id.confirmButton);
        confirmPayment.setOnClickListener((view)->{goToRating();});
    }

    private void goToRating(){
        startActivity(new Intent(this, RatingAcitivity.class));
    }
}
