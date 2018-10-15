package com.unimelb.droptable.echo.clientTaskManagement;

public class Utility {

    public static ImmutableTask.Builder currentTaskBuilder = ImmutableTask.builder()
            .title("Placeholder Title")
            .address("Placeholder Address")
            .category("Placeholder Category")
            .subCategory("Placeholder Subcategory")
            .notes("Placeholder Notes")
            .paymentAmount("Placeholder Amount");

    /**
     * Given two user names, creates a unique chat ID for them, which simply involves sorting them
     * alphabetically and adding a hyphen between them. Hence, it doesn't matter in what order the
     * two arguments are given.
     * @param user1 a username
     * @param user2 another username
     * @return a unique chat id for the two given users.
     */
    public static String generateUserChatId(String user1, String user2) {
        String childName;

        if (user1.compareTo(user2) < 0) {
            childName = String.format("%s-%s", user1, user2);
        } else {
            childName = String.format("%s-%s", user2, user1);
        }

        return childName;
    }
}
