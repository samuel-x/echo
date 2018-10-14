package com.unimelb.droptable.echo.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.unimelb.droptable.echo.ChatMessage;
import com.unimelb.droptable.echo.ClientInfo;
import com.unimelb.droptable.echo.R;
import com.unimelb.droptable.echo.activities.tasks.uiElements.TaskNotification;
import com.unimelb.droptable.echo.clientTaskManagement.FirebaseAdapter;
import com.unimelb.droptable.echo.clientTaskManagement.Utility;

public class ChatActivity extends AppCompatActivity {

    public String currentChatPartner;

    protected FloatingActionButton sendButton;
    protected EditText inputText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Get references to UI elements.
        inputText = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> onMessageSend());

        // Figure out who we're speaking with.
        currentChatPartner = getIntent().getStringExtra(getString(R.string.chat_partner));

        // Retrieve and show existing messages.
        displayMessages(Utility.generateUserChatId(ClientInfo.getUsername(), currentChatPartner));
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
    }

    /**
     * Populates the list view with chat messages for the given id.
     * @param chatId a string representing the chat id used by Firebase.
     */
    private void displayMessages(String chatId) {
        ListView messageList = findViewById(R.id.messageList);
        FirebaseListOptions<ChatMessage> options = new FirebaseListOptions.Builder<ChatMessage>()
                .setQuery(FirebaseAdapter.messagesDbReference.child(chatId), ChatMessage.class)
                .setLifecycleOwner(this)
                .setLayout(R.layout.chat_message)
                .build();
        FirebaseListAdapter<ChatMessage> listAdapter =
                new FirebaseListAdapter<ChatMessage>(options) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to elements in the message layout.
                TextView messageText = v.findViewById(R.id.messageText);
                TextView messageUser = v.findViewById(R.id.senderName);
                TextView messageTime = v.findViewById(R.id.messageTime);

                // Set the text, including message date.
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getSender());
                messageTime.setText(DateFormat.format("HH:mm, dd-MM-yyyy",
                        model.getMessageTime()));

                // Tint the background for the message if *this* user sent it.
                if (model.getSender().equals(ClientInfo.getUsername())) {
                    v.setBackgroundResource(R.color.messageSenderColor);
                }
            }
        };

        messageList.setAdapter(listAdapter);
    }

    /**
     * A method which, when called, sends the current message.
     */
    protected void onMessageSend() {
        if (inputText.getText().toString().equals("")) {
            // There's nothing to send. Don't send anything.
            return;
        }

        sendMessage();

        // Clear the input field.
        inputText.setText("");
    }

    /**
     * A method to separate Firebase functions when testing
     */
    protected void sendMessage() {
        // There's something to send. Create a ChatMessage and push it.
        FirebaseAdapter.pushMessage(new ChatMessage(inputText.getText().toString(),
                ClientInfo.getUsername(), currentChatPartner));
    }
}