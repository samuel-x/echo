package com.unimelb.droptable.echo.activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int MIN_USERNAME_LENGTH = 3;
    private static final int PHONENUMBER_LENGTH = 10;
    private static String ASSISTANT_AP_STATUS_FAIL;
    private static String PHONE_FAIL;
    private static String NAME_FAIL;
    private static String SUCCESSFUL_LOGIN;
    protected PlaceAutocompleteFragment address;

    // UI references.
    protected EditText usernameText;
    protected EditText phoneNumberText;
    protected CheckBox isAssistantCheckBox;
    protected Button signInButton;

    protected FloatingActionButton helperButton;
    protected View signInView;

    private Place userHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // User has not selected their home
        userHome = null;

        // Set up the login form.
        usernameText = findViewById(R.id.usernameText);
        isAssistantCheckBox = findViewById(R.id.isAssistantCheckBox);
        phoneNumberText = findViewById(R.id.phoneNumberText);

        // Get a reference to the sign in button and set its listener.
        signInButton = findViewById(R.id.signInButton);
        signInButton.setOnClickListener((view) -> onSignInClick());

        // Get a reference to the helper button and set its listener.
        helperButton = findViewById(R.id.loginHelperButton);
        helperButton.setOnClickListener(view -> onHelperPress());

        signInView = findViewById(R.id.signInView);

        // Define constants from resources.
        ASSISTANT_AP_STATUS_FAIL = getString(R.string.invalid_assistant_or_ap);
        PHONE_FAIL = getString(R.string.invalid_phone_number);
        NAME_FAIL = getString(R.string.invalid_user_name);
        SUCCESSFUL_LOGIN = getString(R.string.successful_login);

        FirebaseAdapter.goOnline();

        address = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.textHomeAddress);

        //Request for Location permissions
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);

        address.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // Assign their home.
                userHome = place;
            }

            @Override
            public void onError(Status status) {
                Toast.makeText(LoginActivity.this,
                        status.getStatusMessage().toString(), Toast.LENGTH_LONG).show();
            }
        });
        address.setHint(getString(R.string.prompt_home_address));
    }




    protected void onSignInClick() {
        try {
            attemptLogin();
        } catch (LoginError loginError) {
            Toast.makeText(this, loginError.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    protected void attemptLogin() throws LoginError {

        showProgress(true);

        String username = usernameText.getText().toString();
        String phoneNumber = phoneNumberText.getText().toString();

        // Verify that the user name is valid.
        if (!isUsernameValid(username)) {
            showProgress(false);
            throw new LoginError(NAME_FAIL);
        }

        // Verify that the phone number is valid.
        if(!isPhoneNumberValid(phoneNumber)) {
            showProgress(false);
            throw new LoginError(PHONE_FAIL);
        }

        // Verify that the isAssistant checkbox matches the boolean on the database, if the username
        // already exists.
        if (FirebaseAdapter.userExists(username)
                && isAssistantCheckBox.isChecked() != FirebaseAdapter.isAssistant(username)) {
            showProgress(false);
            throw new LoginError(ASSISTANT_AP_STATUS_FAIL);
        }

        if (userHome == null) {
            FirebaseAdapter.pushUser(username, phoneNumber, isAssistantCheckBox.isChecked());
        }
        else {
            FirebaseAdapter.pushUser(username, phoneNumber, isAssistantCheckBox.isChecked(),
                    userHome);
        }
        ClientInfo.setUsername(username);
        ClientInfo.setPhoneNumber(phoneNumber);
        ClientInfo.setIsAssistant(isAssistantCheckBox.isChecked());

        // Get our token for FCM
        if (ClientInfo.getToken() != null) {
            FirebaseAdapter.updateRegistrationToServer(ClientInfo.getToken(), username);
        }
        else {
            FirebaseInstanceId.getInstance().getInstanceId()
                    .addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            return;
                        }

                        // Get new Instance ID token
                        String token = task.getResult().getToken();

                        ClientInfo.setCurrentToken(token);
                        FirebaseAdapter.updateRegistrationToServer(token, username);
                    });
        }

        // Switch to the appropriate activity.
        if (ClientInfo.isAssistant()) {
            startActivity(new Intent(this, AssistantMapActivity.class));
        } else {
            startActivity(new Intent(this, ApMapActivity.class));
        }

        // Show a small successful login message.
        Toast.makeText(LoginActivity.this, SUCCESSFUL_LOGIN, Toast.LENGTH_SHORT).show();
        finish();
    }

    protected boolean isUsernameValid(String username) {
        return username.length() >= MIN_USERNAME_LENGTH;
    }

    protected boolean isPhoneNumberValid(String phoneNumber) {
        return phoneNumber.length() == PHONENUMBER_LENGTH
                && phoneNumber.matches("[0-9]+")
                && phoneNumber.startsWith("04");
    }

    protected void onHelperPress() {
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
    protected void showProgress(final boolean show) {
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

    private class LoginError extends Exception {

        public LoginError() { super(); }

        public LoginError(String message) { super(message); }

    }
}
