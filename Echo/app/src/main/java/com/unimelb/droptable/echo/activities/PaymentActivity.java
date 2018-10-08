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
    protected TextView amount;
    protected ImmutableTask currentTask;




    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        taskCurrentTitle = findViewById(R.id.taskTitle);
        details = findViewById(R.id.details);
        amount = findViewById(R.id.amount);
        taskCurrentTitle.setText("");



        currentTask = FirebaseAdapter.getCurrentTask();

        String title = currentTask.getTitle();
        String details = currentTask.getNotes();
        String amount = currentTask



        //taskCurrentAddress = findViewById(R.id.textTaskInProgressAddress);
        //taskCurrentTime = findViewById(R.id.textTaskInProgressTime);
        //taskCurrentNotes = findViewById(R.id.textTaskInProgressNotes);
        confirmPayment = findViewById(R.id.confirmButton);
        confirmPayment.setOnClickListener((view)->{goToRating();});






    }

    private void goToRating(){
        startActivity(new Intent(this, RatingActivity.class));
    }

    // TODO: Update view with information from database about completed task






}
