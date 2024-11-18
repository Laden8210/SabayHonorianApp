package com.example.sabayhonorianapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Coordination implements Parcelable {

    private double longitude;
    private double latitude;

    public Coordination() {
    }

    public Coordination(double longitude, double latitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    protected Coordination(Parcel in) {
        longitude = in.readDouble();
        latitude = in.readDouble();
    }

    public static final Creator<Coordination> CREATOR = new Creator<Coordination>() {
        @Override
        public Coordination createFromParcel(Parcel in) {
            return new Coordination(in);
        }

        @Override
        public Coordination[] newArray(int size) {
            return new Coordination[size];
        }
    };

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
    }
}
