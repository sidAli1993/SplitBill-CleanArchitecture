package com.poc.paphoscafe.service.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NotificationBody {
    @SerializedName("badge")
    @Expose
    private int badge;

    @SerializedName("sound")
    @Expose
    private String sound;

    @SerializedName("body")
    @Expose
    private String body;

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
