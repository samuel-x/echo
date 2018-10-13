package com.unimelb.droptable.echo;

import com.google.android.gms.location.places.Place;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class ClientInfo {
    private static String username;
    private static boolean isAssistant;
    private static ImmutableTask currentTask;
    private static String phoneNumber;
    private static Place currentPlace;
    private static String currentToken;

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
        return isAssistant() || currentTask.getAssistant() != null;
    }

    public static void updateAssistant(String assistant) {
        currentTask = ImmutableTask.builder().from(currentTask).assistant(assistant).build();
    }

    public static void setCurrentPlace(Place place) { ClientInfo.currentPlace = place; }

    public static Place getCurrentPlace() {
        return currentPlace;
    }
}
