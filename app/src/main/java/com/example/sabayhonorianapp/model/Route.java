package com.example.sabayhonorianapp.model;


import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


public class Route implements Parcelable {
    private String startingName;
    private String endingName;
    private Coordination startingCoordination;

    private Coordination endingCoordination;

    public Route() {
    }

    public String getStartingName() {
        return startingName;
    }

    public void setStartingName(String startingName) {
        this.startingName = startingName;
    }

    public String getEndingName() {
        return endingName;
    }

    public void setEndingName(String endingName) {
        this.endingName = endingName;
    }

    public Coordination getStartingCoordination() {
        return startingCoordination;
    }

    public void setStartingCoordination(Coordination startingCoordination) {
        this.startingCoordination = startingCoordination;
    }

    public Coordination getEndingCoordination() {
        return endingCoordination;
    }

    public void setEndingCoordination(Coordination endingCoordination) {
        this.endingCoordination = endingCoordination;
    }

    protected Route(Parcel in) {
        startingName = in.readString();
        endingName = in.readString();
        startingCoordination = in.readParcelable(Coordination.class.getClassLoader());
        endingCoordination = in.readParcelable(Coordination.class.getClassLoader());
    }

    public static final Creator<Route> CREATOR = new Creator<Route>() {
        @Override
        public Route createFromParcel(Parcel in) {
            return new Route(in);
        }

        @Override
        public Route[] newArray(int size) {
            return new Route[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(startingName);
        dest.writeString(endingName);
        dest.writeParcelable(startingCoordination, flags);
        dest.writeParcelable(endingCoordination, flags);
    }
}
