package com.unimelb.droptable.echo.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.Button;
import android.widget.TextView;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;

public class HelperActivity extends AppCompatActivity {

    private static String currentHelperText;

    private TextView helperText;
    private Button okButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper);

        // Get references to UI elements.
        helperText = findViewById(R.id.helpText);
        helperText.setMovementMethod(new ScrollingMovementMethod());

        // Get ok button and set its listener.
        okButton = findViewById(R.id.okButton);
        okButton.setOnClickListener(view -> {onOkPress();});

        // Set helper text.
        helperText.setText(currentHelperText);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ClientInfo.hasPartner()) {
            // Try to attach a chat listener.
            MessageNotification.AttachListener(HelperActivity.this);
        }
    }

    public static void setCurrentHelperText(String currentHelperText) {
        HelperActivity.currentHelperText = currentHelperText;
    }

    private void onOkPress() {
        finish();
    }
}