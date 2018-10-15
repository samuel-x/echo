package com.unimelb.droptable.echo;

import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;

import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class ClientInfo {
    private static String username;
    private static Boolean isAssistant;
    private static ImmutableTask currentTask;
    private static String phoneNumber;
    private static Place currentPlace;
    private static String currentToken;
    private static Float rating;
    private static Location currentLocation;

    public static Location getCurrentLocation() {
        return currentLocation;
    }

    public static void setCurrentLocation(Location location) {
        currentLocation = location;
    }

    public static LatLng getCurrentLocationAsLatLng() {
        return new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
    }

    public static float getRating() {
        if (isAssistant()) {
            try {
                rating = FirebaseAdapter.getUserRating(ClientInfo.getUsername());
            } catch (Exception e){
                return 0.0f;
            }
            return rating;
        }
        else {
            return 0.0f;
        }
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ClientInfo.username = username;
    }

    public static void setCurrentToken(String token) {
        ClientInfo.currentToken = token;
    }

    public static boolean isAssistant() {
        return isAssistant;
    }

    public static void setIsAssistant(boolean isAssistant) {
        ClientInfo.isAssistant = isAssistant;
    }

    public static ImmutableTask getTask() {
        return currentTask;
    }

    public static void updateTask() {
        currentTask = FirebaseAdapter.getCurrentTask();
    }

    public static boolean hasTask() {
        return currentTask != null;
    }

    public static void setTask(ImmutableTask task) {
        ClientInfo.currentTask = task;
    }

    public static String getPhoneNumber() {return phoneNumber;}

    public static void setPhoneNumber(String phoneNumber) { ClientInfo.phoneNumber = phoneNumber;}

    public static String getToken() {
        return currentToken;
    }

    public static boolean hasPartner() {
        return currentTask != null && (isAssistant() || currentTask.getAssistant() != null);
    }

    public static void updateAssistant(String assistant) {
        currentTask = ImmutableTask.builder().from(currentTask).assistant(assistant).build();
    }

    public static void setCurrentPlace(Place place) { ClientInfo.currentPlace = place; }

    public static Place getCurrentPlace() {
        return currentPlace;
    }

    public static void resetClientInfo() {
        username = null;
        isAssistant = null;
        currentTask = null;
        phoneNumber = null;
        currentToken = null;
        rating = null;
    }
}
