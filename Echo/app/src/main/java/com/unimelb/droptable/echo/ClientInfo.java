package com.unimelb.droptable.echo;

public class ClientInfo {
    private static String username;
    private static boolean isAssistant;

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
}
