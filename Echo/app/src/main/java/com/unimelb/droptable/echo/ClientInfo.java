package com.unimelb.droptable.echo;

import android.util.Log;

import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class ClientInfo {
    private static String username;
    private static Boolean isAssistant;
    private static ImmutableTask currentTask;
    private static String phoneNumber;
    private static String currentToken;
    private static Float rating;

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

    public static void resetClientInfo() {
        username = null;
        isAssistant = null;
        currentTask = null;
        phoneNumber = null;
        currentToken = null;
        rating = null;
    }
}
