package com.unimelb.droptable.echo.activities.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.HelperActivity;
import com.unimelb.droptable.echo.activities.HomePlaceholder;

/**
 * A login screen that offers login via a simple username.
 */
public class LoginActivity extends AppCompatActivity {
    public static final int MIN_USERNAME_LENGTH = 3;

    // UI references.
    private EditText usernameText;
    private CheckBox isAssistantCheckBox;
    private Button signInButton;
    private View signInView;
    private FloatingActionButton helperButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get references to UI elements.
        usernameText = findViewById(R.id.usernameText);
        isAssistantCheckBox = findViewById(R.id.isAssistantCheckBox);

        // Get a reference to the sign in button and set its listener.
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener((view) -> {attemptLogin();});

        // Get a reference to the helper button and set its listener.
        helperButton = findViewById(R.id.helperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});

        // Get a reference to the whole view.
        signInView = findViewById(R.id.signInView);
    }

    /**
     * Attempts to sign in with the given sign in information. If successful, switches activity.
     */
    private void attemptLogin() {
        showProgress(true);

        String username = usernameText.getText().toString();
        if (!isUsernameValid(username)) {
            showProgress(false);
            return;
        }

        // The login attempt is valid. Remember the entered information.
        ClientInfo.setUsername(username);
        ClientInfo.setIsAssistant(isAssistantCheckBox.isChecked());

        // Switch to welcome/home screen.
        startActivity(new Intent(this, HomePlaceholder.class));
    }

    /**
     * Checks if the given username is valid.
     * @param username The username being checked.
     * @return A boolean to indicate if the given username is valid or not.
     */
    private boolean isUsernameValid(String username) {
        return username.length() >= MIN_USERNAME_LENGTH;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        signInView.setVisibility(show ? View.GONE : View.VISIBLE);
        signInView.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                signInView.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });
    }

    private void onHelperPress() {
        HelperActivity.setCurrentHelperText(
                String.format(
                        getString(R.string.help_login_text),
                        MIN_USERNAME_LENGTH,
                        getString(R.string.prompt_assistant),
                        getString(R.string.sign_in_button)
                ));
        startActivity(new Intent(this, HelperActivity.class));
    }
}

