package com.poc.paphoscafe.retrofit.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DTORegister {
    @SerializedName("scan_id")
    @Expose
    String scanId;

    @SerializedName("fcm_token")
    @Expose
    String fcmToken;

    @SerializedName("status")
    @Expose
    int status = 0;

    @SerializedName("device_id")
    @Expose
    String deviceId;

    @SerializedName("device_title")
    @Expose
    String deviceTitle;

    public String getScanId() {
        return scanId;
    }

    public void setScanId(String scanId) {
        this.scanId = scanId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceTitle() {
        return deviceTitle;
    }

    public void setDeviceTitle(String deviceTitle) {
        this.deviceTitle = deviceTitle;
    }
}