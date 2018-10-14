package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class PaymentActivity extends AppCompatActivity {

    private Button confirmPayment;

    protected TextView taskCurrentTitle;
    protected TextView details;
    protected TextView paymentAmount;
    protected ImmutableTask currentTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

//        currentTask = FirebaseAdapter.getCurrentTask();
//        String title = currentTask.getTitle();

//        paymentAmount = findViewById(R.id.paymentAmount);
//
//        paymentAmount.setText("Fuck my ass");

        confirmPayment = findViewById(R.id.submit);
        confirmPayment.setOnClickListener((view)->{goToMap();});

    }

    private void goToMap() {
        startActivity(new Intent(this, ApMapActivity.class));
    }

    private void populate(){

    }


    // TODO: Update view with information from database about completed task


}
