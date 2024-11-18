package com.example.sabayhonorianapp.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class UserAccount implements Parcelable {

    private String userUID;
    private String firstName;
    private String middleName;
    private String lastName;
    private String gender;
    private Date birthDate;



    public UserAccount() {
    }

    public UserAccount(String userUID, String firstName, String middleName, String lastName, String gender, Date birthDate, String userType) {
        this.userUID = userUID;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.gender = gender;
        this.birthDate = birthDate;

    }

    protected UserAccount(Parcel in) {
        userUID = in.readString();
        firstName = in.readString();
        middleName = in.readString();
        lastName = in.readString();
        gender = in.readString();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userUID);
        dest.writeString(firstName);
        dest.writeString(middleName);
        dest.writeString(lastName);
        dest.writeString(gender);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserAccount> CREATOR = new Creator<UserAccount>() {
        @Override
        public UserAccount createFromParcel(Parcel in) {
            return new UserAccount(in);
        }

        @Override
        public UserAccount[] newArray(int size) {
            return new UserAccount[size];
        }
    };

    public String getUserUID() {
        return userUID;
    }

    public void setUserUID(String userUID) {
        this.userUID = userUID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }
}
