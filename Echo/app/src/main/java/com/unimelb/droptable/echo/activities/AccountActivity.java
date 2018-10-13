package com.unimelb.droptable.echo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;

public class AccountActivity extends AppCompatActivity {

    private ImageView avatar;
    private TextView isAssistantText;
    private TextView username;
    private TextView phone;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);

        avatar = findViewById(R.id.accountAvatar);
        isAssistantText = findViewById(R.id.textAccountIsAssistant);
        username = findViewById(R.id.textAccountUsername);
        phone = findViewById(R.id.textAccountPhoneNumber);
        ratingBar = findViewById(R.id.userRatingAccount);
        ratingBar.setIsIndicator(true);

        updateUI();
    }

    private void updateUI() {
        username.setText(ClientInfo.getUsername());
        phone.setText(ClientInfo.getPhoneNumber());
        if (ClientInfo.isAssistant()) {
            ratingBar.setRating(ClientInfo.getRating());
            isAssistantText.setEnabled(true);
            ratingBar.setEnabled(true);
        }
        else {
            isAssistantText.setEnabled(false);
            isAssistantText.setAlpha(0.0f);
            ratingBar.setEnabled(false);
            ratingBar.setAlpha(0.0f);
        }
    }

}
