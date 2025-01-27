package com.example.webchatapp;

public class MessageModelClass {
    private String messageId;
    private String message;
    private String senderid;
    private String receiverid; // Tambahkan field receiverid
    private long timeStamp;
    private boolean isDeleted = false;
    private long deletedAt;

    // Default constructor
    public MessageModelClass() {
    }

    // Constructor dengan receiverid
    public MessageModelClass(String message, String senderid, long timeStamp) {
//        this.messageId = messageId;
        this.message = message;
        this.senderid = senderid;
//        this.receiverid = receiverid;
        this.timeStamp = timeStamp;
    }

    // Getter dan Setter untuk receiverid
    public String getReceiverid() {
        return receiverid;
    }

    public void setReceiverid(String receiverid) {
        this.receiverid = receiverid;
    }

    // Existing getters and setters...
    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderid() {
        return senderid;
    }

    public void setSenderid(String senderid) {
        this.senderid = senderid;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }


    // Konstruktor, getter, setter
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}