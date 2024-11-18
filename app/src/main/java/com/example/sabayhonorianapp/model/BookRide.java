package com.example.sabayhonorianapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class BookRide implements Parcelable {

    private String rideId;
    private String userId;
    private String postRideId;
    private String status;

    public BookRide() {
    }

    public BookRide(String rideId, String userId, String postRideId, String status) {
        this.rideId = rideId;
        this.userId = userId;
        this.postRideId = postRideId;
        this.status = status;
    }

    protected BookRide(Parcel in) {
        rideId = in.readString();
        userId = in.readString();
        postRideId = in.readString();
        status = in.readString();
    }

    public static final Creator<BookRide> CREATOR = new Creator<BookRide>() {
        @Override
        public BookRide createFromParcel(Parcel in) {
            return new BookRide(in);
        }

        @Override
        public BookRide[] newArray(int size) {
            return new BookRide[size];
        }
    };

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPostRideId() {
        return postRideId;
    }

    public void setPostRideId(String postRideId) {
        this.postRideId = postRideId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(rideId);
        parcel.writeString(userId);
        parcel.writeString(postRideId);
        parcel.writeString(status);
    }
}
