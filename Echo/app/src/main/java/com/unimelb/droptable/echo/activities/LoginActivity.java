package com.unimelb.droptable.echo.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int PHONENUMBER_LENGTH = 10;

    // UI references.
    private EditText usernameText;
    private EditText phoneNumberText;
    private CheckBox isAssistantCheckBox;
    private Button signInButton;
    private FloatingActionButton helperButton;
    private View signInView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        usernameText = findViewById(R.id.usernameText);
        isAssistantCheckBox = findViewById(R.id.isAssistantCheckBox);
        phoneNumberText = findViewById(R.id.phoneNumberText);

        // Get a reference to the sign in button and set its listener.
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener((view) -> {attemptLogin();});

        // Get a reference to the helper button and set its listener.
        helperButton = findViewById(R.id.loginHelperButton);
        helperButton.setOnClickListener(view -> {onHelperPress();});

        signInView = findViewById(R.id.signInView);

        FirebaseAdapter.goOnline();
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        showProgress(true);

        String username = usernameText.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();

        // Verify that the user name is valid.
        if (!isUsernameValid(username)) {
            showProgress(false);
            // TODO: Show error message.
            Log.d("Login", "Denied access based on invalid user name");
            return;
        }

        // Verify that the isAssistant checkbox matches the boolean on the database, if the username
        // already exists.
        if (FirebaseAdapter.userExists(username)
                && isAssistantCheckBox.isChecked() != FirebaseAdapter.getIsAssistant(username)) {
            showProgress(false);
            // TODO: Show error message.
            Log.d("Login", "Denied access based on user type for existing user");
            return;
        }

        FirebaseAdapter.pushUser(username, isAssistantCheckBox.isChecked());
        ClientInfo.setUsername(username);
        ClientInfo.setIsAssistant(isAssistantCheckBox.isChecked());


        // if phonenumber valid update db
        if(isPhoneNumberValid(phoneNumber)){
            Log.d("Login","Phone number: " + phoneNumber + " is valid");
            ClientInfo.setPhoneNumber(phoneNumber);
            FirebaseAdapter.pushPhoneNumber(username,phoneNumber);
        }else {
            Log.d("Login","Phone number: " + phoneNumber + " is not valid");
        }
        // TODO
        String working = FirebaseAdapter.getPhoneNumber(username);
        if(working == null){
            System.out.println("Phone number was not added");
        } else {
            System.out.println(working + "was added to the firebase");
            System.out.println("the number added was correct? " + (working.equals(phoneNumber)));
        }
        // TODO
        // Switch to the appropriate activity.
        if (ClientInfo.isAssistant()) {
            startActivity(new Intent(this, AssistantMapActivity.class));
        } else {
            startActivity(new Intent(this, ApMapActivity.class));
        }

        finish();
    }

    private boolean isUsernameValid(String username) {
        return username.length() >= MIN_USERNAME_LENGTH;
    }

    private boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == PHONENUMBER_LENGTH
                && phoneNumber.matches("[0-9]+")
                && phoneNumber.startsWith("04");
    }

    private void onHelperPress() {
        HelperActivity.setCurrentHelperText(
                String.format(
                        getString(R.string.login_help_text),
                        MIN_USERNAME_LENGTH,
                        getString(R.string.prompt_assistant),
                        getString(R.string.sign_in_button)
                ));
        startActivity(new Intent(this, HelperActivity.class));
    }

    /**
     * Shows the progress UI and hides the login form. Partially auto-generated by Android Studio.
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
