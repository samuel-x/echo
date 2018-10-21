package com.unimelb.droptable.echo.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.uiElements.MessageNotification;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;

public class AccountActivity extends AppCompatActivity {

    protected ImageView avatar;
    protected TextView isAssistantText;
    protected TextView username;
    protected TextView phone;
    protected RatingBar ratingBar;
    protected Button changeAddress;

    protected PlaceAutocompleteFragment homeAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_screen);

        avatar = findViewById(R.id.accountAvatar);
        isAssistantText = findViewById(R.id.textAccountIsAssistant);
        username = findViewById(R.id.textAccountUsername);
        phone = findViewById(R.id.textAccountPhoneNumber);
        ratingBar = findViewById(R.id.userRatingAccount);
        changeAddress = findViewById(R.id.userChangeHomeAddress);
        changeAddress.setOnClickListener(view -> onChangeAddress());
        ratingBar.setIsIndicator(true);

        updateUI();
    }

    private void onChangeAddress() {

        android.app.AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new android.app.AlertDialog.Builder(this,
                    android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new android.app.AlertDialog.Builder(this);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setView(R.layout.home_address)
                    .setTitle("Change Address")
                    .setMessage("Please select your address below.")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                    })
                    .show();
            homeAddress = (PlaceAutocompleteFragment)
                    getFragmentManager().findFragmentById(R.id.textHomeAddressModifyDialog);

            homeAddress.setOnPlaceSelectedListener(new PlaceSelectionListener() {
                @Override
                public void onPlaceSelected(Place place) {
                    // Assign their home.
                    FirebaseAdapter.updateHomeAddress(ClientInfo.getUsername(), place);
                    return;
                }

                @Override
                public void onError(Status status) {
                    Toast.makeText(AccountActivity.this,
                            status.getStatusMessage().toString(), Toast.LENGTH_LONG).show();
                }
            });
        } else {
            builder.setTitle("Error").setMessage("Your version of android is too low. " +
                    "Please update your phone.");
        }
    }

    @Override
    protected void onResume() {
        // Attach our task listener
        super.onResume();
        if (ClientInfo.hasTask()) {
            if (ClientInfo.isAssistant()) {
                try {
                    TaskNotification.AttachAssistantListener(this);
                } catch (TaskNotification.IncorrectListenerException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    TaskNotification.AttachAPListener(this);
                } catch (TaskNotification.IncorrectListenerException e) {
                    e.printStackTrace();
                }
            }
        }

        if (ClientInfo.hasPartner()) {
            // Try to attach a chat listener.
            MessageNotification.AttachListener(this);
        }
    }

    protected void updateUI() {
        username.setText(ClientInfo.getUsername());
        phone.setText(ClientInfo.getPhoneNumber());
        if (ClientInfo.isAssistant()) {
            ratingBar.setRating(ClientInfo.getRating());
            isAssistantText.setEnabled(true);
            ratingBar.setEnabled(true);
            changeAddress.setEnabled(false);
            changeAddress.setAlpha(0.0f);
        }
        else {
            isAssistantText.setEnabled(false);
            isAssistantText.setAlpha(0.0f);
            ratingBar.setEnabled(false);
            ratingBar.setAlpha(0.0f);
            changeAddress.setEnabled(true);
        }
    }

}
