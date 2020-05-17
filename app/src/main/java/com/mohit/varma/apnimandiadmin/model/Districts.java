package com.mohit.varma.apnimandiadmin.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Districts implements Serializable {
    private int districtPinCode;
    private String districtName;
    private String districtAbbr;
    private Map<Integer,String> wardMap = new HashMap<>();

    public Districts() {
    }

    public int getDistrictPinCode() {
        return districtPinCode;
    }

    public void setDistrictPinCode(int districtPinCode) {
        this.districtPinCode = districtPinCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictAbbr() {
        return districtAbbr;
    }

    public void setDistrictAbbr(String districtAbbr) {
        this.districtAbbr = districtAbbr;
    }

    public Map<Integer, String> getWardMap() {
        return wardMap;
    }

    public void setWardMap(Map<Integer, String> wardMap) {
        this.wardMap = wardMap;
    }
}
