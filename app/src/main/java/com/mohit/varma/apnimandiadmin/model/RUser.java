package com.mohit.varma.apnimandiadmin.model;

import java.io.Serializable;
import java.util.List;

public class RUser implements Serializable {
    private String userId;
    private String name;
    private String phoneNumber;
    private List<RationRequests> rationRequestsList;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public List<RationRequests> getRationRequestsList() {
        return rationRequestsList;
    }

    public void setRationRequestsList(List<RationRequests> rationRequestsList) {
        this.rationRequestsList = rationRequestsList;
    }
}
