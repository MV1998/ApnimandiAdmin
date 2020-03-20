package com.mohit.varma.apnimandiadmin.model;

public class User {

    private String mUserId;
    private String mFirstName;
    private String mLastName;
    private String mCityAddress;
    private String mAddress;
    private String mAge;
    private String mPhoneNumber;
    private String mZipCode;

    public User(String mUserId, String mFirstName, String mLastName, String mCityAddress, String mAddress, String mAge, String mPhoneNumber, String mZipCode) {
        this.mUserId = mUserId;
        this.mFirstName = mFirstName;
        this.mLastName = mLastName;
        this.mCityAddress = mCityAddress;
        this.mAddress = mAddress;
        this.mAge = mAge;
        this.mPhoneNumber = mPhoneNumber;
        this.mZipCode = mZipCode;
    }

    public String getmUserId() {
        return mUserId;
    }

    public void setmUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getmFirstName() {
        return mFirstName;
    }

    public void setmFirstName(String mFirstName) {
        this.mFirstName = mFirstName;
    }

    public String getmLastName() {
        return mLastName;
    }

    public void setmLastName(String mLastName) {
        this.mLastName = mLastName;
    }

    public String getmCityAddress() {
        return mCityAddress;
    }

    public void setmCityAddress(String mCityAddress) {
        this.mCityAddress = mCityAddress;
    }

    public String getmAddress() {
        return mAddress;
    }

    public void setmAddress(String mAddress) {
        this.mAddress = mAddress;
    }

    public String getmAge() {
        return mAge;
    }

    public void setmAge(String mAge) {
        this.mAge = mAge;
    }

    public String getmPhoneNumber() {
        return mPhoneNumber;
    }

    public void setmPhoneNumber(String mPhoneNumber) {
        this.mPhoneNumber = mPhoneNumber;
    }

    public String getmZipCode() {
        return mZipCode;
    }

    public void setmZipCode(String mZipCode) {
        this.mZipCode = mZipCode;
    }
}
