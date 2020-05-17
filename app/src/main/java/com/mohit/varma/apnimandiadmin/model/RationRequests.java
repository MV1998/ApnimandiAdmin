package com.mohit.varma.apnimandiadmin.model;

import java.io.Serializable;

class RationRequests extends RUser implements Serializable {
    private String rationCardNumber;
    private String rationWadNo;
    private String rationRequestCode;
    private String requestTime;
    private boolean requestStatus;

    public String getRationCardNumber() {
        return rationCardNumber;
    }

    public void setRationCardNumber(String rationCardNumber) {
        this.rationCardNumber = rationCardNumber;
    }

    public String getRequestTime() {
        return requestTime;
    }

    public void setRequestTime(String requestTime) {
        this.requestTime = requestTime;
    }

    public boolean isRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(boolean requestStatus) {
        this.requestStatus = requestStatus;
    }

    public String getRationRequestCode() {
        return rationRequestCode;
    }

    public void setRationRequestCode(String rationRequestCode) {
        this.rationRequestCode = rationRequestCode;
    }

    public String getRationWadNo() {
        return rationWadNo;
    }

    public void setRationWadNo(String rationWadNo) {
        this.rationWadNo = rationWadNo;
    }
}
