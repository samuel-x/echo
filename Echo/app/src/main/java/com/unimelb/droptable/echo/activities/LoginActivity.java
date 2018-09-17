package com.unimelb.droptable.echo.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.TaskCreation;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int MIN_USERNAME_LENGTH = 4;

    // UI references.
    private EditText usernameText;
    private CheckBox isAssistantCheckBox;
    private Button signInButton;
    private View signInView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        usernameText = findViewById(R.id.usernameText);
        isAssistantCheckBox = findViewById(R.id.isAssistantCheckBox);

        // Get a reference to the sign in button and set its listener.
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener((view) -> {attemptLogin();});

        signInView = findViewById(R.id.signInView);
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        showProgress(true);

        String username = usernameText.getText().toString();
        if (!isUsernameValid(username)) {
            showProgress(false);
        }

        ClientInfo.setUsername(username);
        ClientInfo.setIsAssistant(isAssistantCheckBox.isChecked());

        // Switch to task creation for testing.
        startActivity(new Intent(this, TaskCreation.class));
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= MIN_USERNAME_LENGTH;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            signInView.setVisibility(show ? View.GONE : View.VISIBLE);
            signInView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    signInView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            signInView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}

