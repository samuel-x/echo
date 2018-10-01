package com.unimelb.droptable.echo;

import com.unimelb.droptable.echo.clientTaskManagement.ImmutableTask;

public class ClientInfo {
    private static String username;
    private static boolean isAssistant;
    private static ImmutableTask currentTask;
    private static String phoneNumber;

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        ClientInfo.username = username;
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

    public static boolean hasTask() {
        return currentTask != null;
    }

    public static void setTask(ImmutableTask task) {
        ClientInfo.currentTask = task;
    }

    public static String getPhoneNumber() {return phoneNumber;}

    public static void setPhoneNumber(String phoneNumber) { ClientInfo.phoneNumber = phoneNumber;}
}
