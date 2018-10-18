package com.unimelb.droptable.echo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;



public class PaymentActivity extends AppCompatActivity {

    private final int DEFAULT_RATING = 5;

    private Button confirmPaymentAndSubmit;

    protected TextView taskTitle;
    protected TextView notes;
    protected TextView paymentAmount;
    protected TextView userRating;
    protected TextView userName;
    protected String ratingText;
    protected RatingBar ratingBar;

    protected ImmutableTask currentTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        currentTask = FirebaseAdapter.getCurrentTask();
        try {
            String title = currentTask.getTitle();
        }
        catch(Exception e){
            String title = "Task";
        }

        ratingBar = findViewById(R.id.ratingBar);

        paymentAmount = findViewById(R.id.paymentAmount);
        String amount = "$"+currentTask.getPaymentAmount();
        paymentAmount.setText(amount);


        userRating = findViewById(R.id.userRating);
        try {
            float rating = FirebaseAdapter.getUserRating(currentTask.getAssistant());
            String ratingText = "Rating: " + Float.toString(rating) + " Stars";

        } catch (Exception e){
            String ratingText = "Rating: " + Integer.toString(DEFAULT_RATING) + " Stars";
        }

        userRating.setText(ratingText);
        userName = findViewById(R.id.userName);
        userName.setText(currentTask.getAssistant());

        taskTitle = findViewById(R.id.taskTitle);
        taskTitle.setText(currentTask.getTitle());

        notes = findViewById(R.id.taskNotes);
        notes.setText(currentTask.getNotes());
        confirmPaymentAndSubmit = findViewById(R.id.submit);
        confirmPaymentAndSubmit.setOnClickListener((view)-> onSubmit());

    }

    protected void onSubmit() {
        completeTask();
    }

    protected void completeTask() {

        ClientInfo.updateTask();
        ImmutableTask task = ClientInfo.getTask();

        FirebaseAdapter.updateUserRating(task.getAssistant(), ratingBar.getRating());
        FirebaseAdapter.completeTask(task);

        ClientInfo.setTask(null);

        goToMap();
        finish();
    }


    protected void goToMap() {

        startActivity(new Intent(this, ApMapActivity.class));
    }

}
