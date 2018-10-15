package com.unimelb.droptable.echo;

import java.util.Date;

public class ChatMessage {

    private String messageText;
    private String sender;
    private String receiver;
    private long messageTime;

    public ChatMessage(String messageText, String sender, String receiver) {
        this.messageText = messageText;
        this.sender = sender;
        this.receiver = receiver;

        // Initialize to current time
        messageTime = new Date().getTime();
    }

    public ChatMessage(){

    }

    public String getMessageText() {
        return messageText;
    }

    public void setMessageText(String messageText) {
        this.messageText = messageText;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(long messageTime) {
        this.messageTime = messageTime;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}