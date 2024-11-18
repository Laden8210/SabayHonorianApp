package com.example.sabayhonorianapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.Timestamp;
import com.mapbox.geojson.Point;

import java.sql.Time;
import java.util.Date;

public class PostRide implements Parcelable {

    private String postUID;
    private String authorUID;
    private String authorName;
    private Date postDate;
    private String description;
    private Timestamp rideTime;
    private Timestamp rideEnd;
    private Timestamp postTime;
    private Date rideDate;
    private String destination;
    private String origin;
    private String status;
    private String vehicleType;
    private int availableSeats;
    private Coordination originCoordination;
    private Coordination destinationCoordination;

    public PostRide() {
    }

    protected PostRide(Parcel in) {
        postUID = in.readString();
        authorUID = in.readString();
        authorName = in.readString();
        description = in.readString();
        rideTime = in.readParcelable(Timestamp.class.getClassLoader());
        rideEnd = in.readParcelable(Timestamp.class.getClassLoader());
        postTime = in.readParcelable(Timestamp.class.getClassLoader());
        destination = in.readString();
        origin = in.readString();
        status = in.readString();
        vehicleType = in.readString();
        availableSeats = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(postUID);
        dest.writeString(authorUID);
        dest.writeString(authorName);
        dest.writeString(description);
        dest.writeParcelable(rideTime, flags);
        dest.writeParcelable(rideEnd, flags);
        dest.writeParcelable(postTime, flags);
        dest.writeString(destination);
        dest.writeString(origin);
        dest.writeString(status);
        dest.writeString(vehicleType);
        dest.writeInt(availableSeats);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PostRide> CREATOR = new Creator<PostRide>() {
        @Override
        public PostRide createFromParcel(Parcel in) {
            return new PostRide(in);
        }

        @Override
        public PostRide[] newArray(int size) {
            return new PostRide[size];
        }
    };

    public String getPostUID() {
        return postUID;
    }

    public void setPostUID(String postUID) {
        this.postUID = postUID;
    }

    public String getAuthorUID() {
        return authorUID;
    }

    public void setAuthorUID(String authorUID) {
        this.authorUID = authorUID;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public Date getPostDate() {
        return postDate;
    }

    public void setPostDate(Date postDate) {
        this.postDate = postDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Timestamp getRideTime() {
        return rideTime;
    }

    public void setRideTime(Timestamp rideTime) {
        this.rideTime = rideTime;
    }

    public Timestamp getRideEnd() {
        return rideEnd;
    }

    public void setRideEnd(Timestamp rideEnd) {
        this.rideEnd = rideEnd;
    }

    public Timestamp getPostTime() {
        return postTime;
    }

    public void setPostTime(Timestamp postTime) {
        this.postTime = postTime;
    }

    public Date getRideDate() {
        return rideDate;
    }

    public void setRideDate(Date rideDate) {
        this.rideDate = rideDate;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public Coordination getOriginCoordination() {
        return originCoordination;
    }

    public void setOriginCoordination(Coordination originCoordination) {
        this.originCoordination = originCoordination;
    }

    public Coordination getDestinationCoordination() {
        return destinationCoordination;
    }

    public void setDestinationCoordination(Coordination destinationCoordination) {
        this.destinationCoordination = destinationCoordination;
    }
}
