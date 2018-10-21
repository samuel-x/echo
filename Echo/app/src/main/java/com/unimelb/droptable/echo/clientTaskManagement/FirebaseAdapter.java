package com.unimelb.droptable.echo.clientTaskManagement;


import android.location.Location;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.unimelb.droptable.echo.ChatMessage;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.Utility;

public class FirebaseAdapter {

    protected static final String TASKS_ROOT = "tasks";
    protected static final String MESSAGES_ROOT = "messages";
    protected static final String USERS_ROOT = "users";
    protected static final String TASK_ID = "taskID";
    protected static final String IS_ASSISTANT = "isAssistant";
    protected static final String PHONE_NUMBER = "phoneNumber";
    protected static final String TOKEN_ROOT = "tokens";
    protected static final String ASSISTANT = "assistant";
    protected static final String CATEGORY = "category";
    protected static final String SUBCATEGORY = "subCategory";
    protected static final String NOTES = "notes";
    protected static final String AP = "ap";
    protected static final String LAST_PHASE = "lastPhase";
    protected static final String LATITUDE = "latitude";
    protected static final String LONGITUDE = "longitude";
    protected static final String TOKEN = "token";
    protected static final String LOCATION = "location";
    protected static final String PAYMENT_AMOUNT = "paymentAmount";
    protected static final String STATUS = "status";
    protected static final String RATING = "rating";
    protected static final String HOME = "home";
    protected static final String ADDRESS = "address";
    protected static final String PENDING = "PENDING";
    protected static final String USER_PENDING = "USER_PENDING";
    protected static final String ACCEPTED = "ACCEPTED";
    protected static final String ID = "id";
    protected static final String TITLE = "title";

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference masterDbReference = database.getReference();
    public static DatabaseReference tasksDbReference = masterDbReference
            .child(TASKS_ROOT);
    public static DatabaseReference messagesDbReference = masterDbReference
            .child(MESSAGES_ROOT);
    public static DatabaseReference usersDbReference = masterDbReference
            .child(USERS_ROOT);

    public static DataSnapshot currentData;
    public static ValueEventListener listener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot snapshot) {
            FirebaseAdapter.currentData = snapshot;
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {
        }
    };

    // Our base query for assistants
    public final static Query mostRecentTasks = tasksDbReference.orderByChild(STATUS).equalTo(PENDING);

    public FirebaseAdapter(DatabaseReference testDatabase, DataSnapshot testSnapshot) {
        // Constructor for the test
        masterDbReference = testDatabase;
        currentData = testSnapshot;
    }

    public FirebaseAdapter() {
    }

    /**
     * Pushes a task to the database and assigns it to the current user
     * @param task the task to push
     */
    public static void pushTask(ImmutableTask task) {
        DatabaseReference pushTask = tasksDbReference.push();
        pushTask.setValue(task);
        pushTask.child(ID).setValue(pushTask.getKey());

        usersDbReference.child(ClientInfo.getUsername()).child(TASK_ID).setValue(pushTask.getKey());
    }

    /**
     * Compares the specified task to the task on Firebase and updates changes accordingly
     * @param task the new task
     * @param id the id of the task to update
     */
    public static void updateTask(ImmutableTask task, String id) {
        DatabaseReference push = tasksDbReference.child(id);
        push.setValue(task);
    }

    /**
     * Updates the status of a task
     */
    public static void updateTaskStatus(String newStatus, String id) {
        tasksDbReference.child(id).child(STATUS).setValue(newStatus);
    }

    /**
     * Updates the assistant id of a task
     */
    public static void updateTaskAssistant(String newAssistant, String id) {
        tasksDbReference.child(id).child(ASSISTANT).setValue(newAssistant);
    }

    /**
     * Returns the task id for the current task that the user is associated with. Retrieves it
     * from Firebase.
     * @return the task ID as a String.
     */
    public static String getCurrentTaskID() {
        if (currentData == null) {
            return null;
        }

        return currentData
                .child(USERS_ROOT)
                .child(ClientInfo.getUsername())
                .child(TASK_ID).getValue(String.class);
    }

    /**
     * This returns a DataSnapshot of the user
     * @return a datasnapshot with the user
     */
    public static DataSnapshot getUser(String user) {
        return currentData.child(USERS_ROOT).child(user);
    }

    /**
     * Checks if the given user exists in Firebase
     * @param username the name of the user
     * @return a boolean to reflect if the user does indeed exist
     */
    public static boolean userExists(String username) {
        return currentData
                .child(USERS_ROOT)
                .hasChild(username);
    }

    /**
     * Checks if the given user is an assistant
     * @param username the username to check
     * @return a Boolean to reflect if the user is indeed an assistant
     */
    public static Boolean isAssistant(String username) {
        if (!userExists(username)) {
            return null;
        }

        return currentData
                .child(USERS_ROOT)
                .child(username)
                .child(IS_ASSISTANT)
                .getValue(Boolean.class);
    }

    /**
     * Pushes a user to the Firebase database.
     * @param username The name of the user
     * @param phoneNumber The phone number of the user
     * @param isAssistant Whether or not the user is an assistant
     */
    public static void pushUser(String username, String phoneNumber, boolean isAssistant) {
        usersDbReference.child(username).child(IS_ASSISTANT).setValue(isAssistant);
        usersDbReference.child(username).child(PHONE_NUMBER).setValue(phoneNumber);
    }

    /**
     * Updates a user's home address
     * @param username
     * @param address
     */
    public static void updateHomeAddress(String username, Place address) {
        usersDbReference.child(username).child(HOME).setValue(address.getLatLng());
    }

    /**
     * Pushes a user to the Firebase database.
     * @param username The name of the user
     * @param phoneNumber The phone number of the user
     * @param isAssistant Whether or not the user is an assistant
     * @param address The Place of the user's home address
     */
    public static void pushUser(String username, String phoneNumber, boolean isAssistant, Place address) {
        usersDbReference.child(username).child(IS_ASSISTANT).setValue(isAssistant);
        usersDbReference.child(username).child(PHONE_NUMBER).setValue(phoneNumber);
        usersDbReference.child(username).child(HOME).setValue(address.getLatLng());
    }

    /**
     * Pushes a new user rating to the given user
     * @param username The user name of the user
     * @param rating The new rating
     */
    public static void updateUserRating(String username, float rating) {
        usersDbReference.child(username).child(RATING).setValue(rating);
    }

    /**
     * Gets the rating of a given user
     * @param username The name of the suer
     * @return The rating of the user
     */
    public static float getUserRating(String username) {
        return getUser(username).child(RATING).getValue(float.class);
    }

    /**
     * Returns the phone number of the given user
     * @param username The name of the user
     * @return The phone number of the user
     */
    public static String getPhoneNumber(String username) {
        if (!currentData.child(USERS_ROOT).hasChild(username)) {
            return null;
        }

        if(!currentData.child(USERS_ROOT).child(username).hasChild(PHONE_NUMBER)){
            return null;
        }

        return currentData
                .child(USERS_ROOT)
                .child(username)
                .child(PHONE_NUMBER)
                .getValue(String.class);
    }

    /**
     * Gets a task object from the database
     * @param id The id of the task to get
     * @return The requested task
     */
    public static ImmutableTask getTask(String id) {
        DataSnapshot taskRef = currentData.child(TASKS_ROOT).child(id);

        String title = taskRef.child(TITLE).getValue(String.class);

        if (title == null) {
            // This task does not exist, so return null.
            return null;
        }

        String address = taskRef.child(ADDRESS).getValue(String.class);
        String category = taskRef.child(CATEGORY).getValue(String.class);
        String subCategory = taskRef.child(SUBCATEGORY).getValue(String.class);
        String notes = taskRef.child(NOTES).getValue(String.class);
        String status = taskRef.child(STATUS).getValue(String.class);
        String ap = taskRef.child(AP).getValue(String.class);
        String assistant = taskRef.child(ASSISTANT).getValue(String.class);
        String paymentAmount = taskRef.child(PAYMENT_AMOUNT).getValue(String.class);
        String latitude = taskRef.child(LATITUDE).getValue(String.class);
        String longitude = taskRef.child(LONGITUDE).getValue(String.class);
        String lastPhase = taskRef.child(LAST_PHASE).getValue(String.class);


        return ImmutableTask.builder()
                .title(title)
                .address(address)
                .category(category)
                .subCategory(subCategory)
                .notes(notes)
                .status(status)
                .ap(ap)
                .assistant(assistant)
                .paymentAmount(paymentAmount)
                .latitude(latitude)
                .longitude(longitude)
                .lastPhase(lastPhase)
                .id(taskRef.getKey()).build();
    }

    /**
     * Get the user's current task (if they have one)
     * @return The user's task
     */
    public static ImmutableTask getCurrentTask() {
        String currentTaskId = getCurrentTaskID();

        if (currentTaskId == null) {
            // This user does not have any task assigned to them.
            return null;
        }

        return getTask(currentTaskId);
    }

    /**
     * Returns a Query for the current user's task
     * @return Query
     */
    public static Query queryCurrentTask() {
        return queryTask(getCurrentTaskID());
    }

    /**
     * Returns a Query for the current user's current chat, if it exists.
     * @return Query
     */
    public static Query queryCurrentChat() {
        ImmutableTask currentTask = ClientInfo.getTask();
        return queryChat(Utility.generateUserChatId(currentTask.getAp(), currentTask.getAssistant()));
    }

    /**
     * Returns a Query for a specified task id
     * @return
     */
    public static Query queryTask(String id) {
        return tasksDbReference.child(id);
    }

    /**
     * Returns a Query for a specified chat id.
     * @return
     */
    public static Query queryChat(String id) {
        return messagesDbReference.child(id);
    }

    /**
     * Makes the data base go offline, removing listeners.
     */
    public static void goOffline() {
        masterDbReference.removeEventListener(FirebaseAdapter.listener);
        masterDbReference.removeEventListener(FirebaseAdapter.listener);
        database.goOffline();
    }

    /**
     * Adds listeners, making the database go online.
     */
    public static void goOnline() {
        masterDbReference.addListenerForSingleValueEvent(FirebaseAdapter.listener);
        masterDbReference.addValueEventListener(FirebaseAdapter.listener);
    }

    /**
     * Pushes a message to Firebase.
     * @param message The message
     */
    public static void pushMessage(ChatMessage message) {
        messagesDbReference.child(Utility.generateUserChatId(message.getSender(),
                message.getReceiver())).push().setValue(message);
    }

    /**
     * Assigns a task of the given id to the given assistant
     * @param assistant The assistant to be assigned
     * @param id The id of the task to assign
     */
    public static void assignTask(String assistant, String id) {
        updateTaskStatus(ACCEPTED, id);
        updateTaskAssistant(assistant, id);
        updateAssistantTask(assistant, id);
    }

    /**
     * Sets the task id of the given assistant
     * @param assistant The assistant to set the task id for
     * @param id The id of the task
     */
    protected static void updateAssistantTask(String assistant, String id) {
        usersDbReference.child(assistant).child(TASK_ID).setValue(id);
    }

    /**
     * If there is a 2-phase task, this will go to the last phase.
     */
    public static void updatePhase() {
        tasksDbReference.child(getCurrentTaskID()).child(LAST_PHASE).setValue(Boolean.toString(true));
    }

    /**
     * Completes the given task. This removes it from the data base, as well as references to its
     * id for the assistant and AP.
     * @param task The task to complete
     */
    public static void completeTask(ImmutableTask task) {
        currentData.child(TASKS_ROOT).child(task.getId()).getRef().removeValue();
        currentData.child(USERS_ROOT).child(task.getAssistant()).child(TASK_ID).getRef().removeValue();
        currentData.child(USERS_ROOT).child(task.getAp()).child(TASK_ID).getRef().removeValue();
    }

    /**
     * Updates the server with a user's token
     * @param token The token of the user
     */
    public static void sendRegistrationToServer(String token) {
        masterDbReference.child(TOKEN_ROOT).child(token).setValue(USER_PENDING);
    }

    /**
     * Assigns a user's token for notifications
     * @param token The token of the user
     * @param user The name of the user
     */
    public static void updateRegistrationToServer(String token, String user) {
        masterDbReference.child(USERS_ROOT).child(user).child(TOKEN).setValue(token);
        masterDbReference.child(TOKEN_ROOT).child(token).setValue(user);
    }

    /**
     * Gets a user's token
     * @param user The name of the user
     * @return The token of the user
     */
    public static String getUserRegistration(String user) {
        return getUser(user).child(TOKEN).getValue(String.class);
    }

    /**
     * Pushes the user's lat long to the Firebase.
     * @param location The location of the user
     */
    public static void updateLocationOfUser(Location location) {
        LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
        usersDbReference.child(ClientInfo.getUsername()).child(LOCATION).setValue(loc);
    }

    /**
     * Gets the location of the assistant
     * @return The lat long of the assitant
     */
    public static LatLng getAssistantLocation() {
        return new LatLng(
                getUser(ClientInfo.getTask().getAssistant()).child(LOCATION)
                        .child(LATITUDE).getValue(double.class),
                getUser(ClientInfo.getTask().getAssistant()).child(LOCATION)
                        .child(LONGITUDE).getValue(double.class));
    }

    /**
     * Gets the home address of the specified user
     * @return The lat long of the user's home
     */
    public static LatLng getHomeAddress(String ap) {
        return new LatLng(
                getUser(ap).child(HOME)
                        .child(LATITUDE).getValue(double.class),
                getUser(ap).child(HOME)
                        .child(LONGITUDE).getValue(double.class));
    }

    /**
     * Gets the current location of the specified user
     * @return The lat long of the user
     */
    public static LatLng getLocation(String user) {
        return new LatLng(
                getUser(user).child(LOCATION)
                        .child(LATITUDE).getValue(double.class),
                getUser(user).child(LOCATION)
                        .child(LONGITUDE).getValue(double.class));
    }
}
