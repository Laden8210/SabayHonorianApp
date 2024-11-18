package com.example.sabayhonorianapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;

public class Message  implements Parcelable {

    private String senderUID;
    private String receiverUID;
    private String message;
    private Timestamp timestamp;
    private boolean isReadByReceiver;
    private boolean isReadBySender;

    public Message() {
    }

    public Message(String senderUID, String receiverUID, String message, Timestamp timestamp, boolean isReadByReceiver, boolean isReadBySender) {
        this.senderUID = senderUID;
        this.receiverUID = receiverUID;
        this.message = message;
        this.timestamp = timestamp;
        this.isReadByReceiver = isReadByReceiver;
        this.isReadBySender = isReadBySender;
    }

    protected Message(Parcel in) {
        senderUID = in.readString();
        receiverUID = in.readString();
        message = in.readString();
        timestamp = in.readParcelable(Timestamp.class.getClassLoader());
        isReadByReceiver = in.readByte() != 0;
        isReadBySender = in.readByte() != 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    public String getSenderUID() {
        return senderUID;
    }

    public void setSenderUID(String senderUID) {
        this.senderUID = senderUID;
    }

    public String getReceiverUID() {
        return receiverUID;
    }

    public void setReceiverUID(String receiverUID) {
        this.receiverUID = receiverUID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isReadByReceiver() {
        return isReadByReceiver;
    }

    public void setReadByReceiver(boolean readByReceiver) {
        isReadByReceiver = readByReceiver;
    }

    public boolean isReadBySender() {
        return isReadBySender;
    }

    public void setReadBySender(boolean readBySender) {
        isReadBySender = readBySender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(senderUID);
        parcel.writeString(receiverUID);
        parcel.writeString(message);
        parcel.writeParcelable(timestamp, i);
        parcel.writeByte((byte) (isReadByReceiver ? 1 : 0));
        parcel.writeByte((byte) (isReadBySender ? 1 : 0));
    }
}
