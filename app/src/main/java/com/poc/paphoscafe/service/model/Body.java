package com.poc.paphoscafe.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Body {
    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("Trans_id")
    @Expose
    private String transId;

    @SerializedName("Price")
    @Expose
    private String price;

    @SerializedName("status")
    @Expose
    private int status;

    @SerializedName("vibrate")
    @Expose
    private int vibrate;

    @SerializedName("sound")
    @Expose
    private int sound;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTransId() {
        return transId;
    }

    public void setTransId(String transId) {
        this.transId = transId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getVibrate() {
        return vibrate;
    }

    public void setVibrate(int vibrate) {
        this.vibrate = vibrate;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }
}
