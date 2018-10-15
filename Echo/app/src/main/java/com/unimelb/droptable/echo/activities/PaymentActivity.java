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
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseNotificationsAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;




public class PaymentActivity extends AppCompatActivity {

    private Button confirmPaymentAndSubmit;

    protected TextView taskTitle;
    protected TextView notes;
    protected TextView paymentAmount;
    protected TextView userRating;
    protected TextView userName;

    private RatingBar ratingBar;

    protected ImmutableTask currentTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);



        currentTask = FirebaseAdapter.getCurrentTask();
        String title = currentTask.getTitle();
//
//
        ratingBar = findViewById(R.id.ratingBar);


        paymentAmount = findViewById(R.id.paymentAmount);
        String amount = "$"+currentTask.getPaymentAmount();
        paymentAmount.setText(amount);


        userRating = findViewById(R.id.userRating);
//        float assistantRating = FirebaseAdapter.getUserRating(currentTask.getAssistant());

        userRating.setText("Rating: 5 Stars");
        userName = findViewById(R.id.userName);
        userName.setText(currentTask.getAssistant());

        taskTitle = findViewById(R.id.taskTitle);
        taskTitle.setText(currentTask.getTitle());

        notes = findViewById(R.id.taskNotes);
        notes.setText(currentTask.getNotes());
        confirmPaymentAndSubmit = findViewById(R.id.submit);
        confirmPaymentAndSubmit.setOnClickListener((view)->{onSubmit();});

    }

    protected void onSubmit() {
        completeTask();
    }

    protected void completeTask() {

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

        goToMap();
        finish();
    }


    private void goToMap() {

        startActivity(new Intent(this, ApMapActivity.class));
    }

    // TODO: Update view with information from database about completed task

}
