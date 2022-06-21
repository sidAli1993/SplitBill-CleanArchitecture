package com.middleware.poc.service.model

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName

class NotificationBody {
    @SerializedName("badge")
    @Expose
    private var badge: Int? = null

    @SerializedName("sound")
    @Expose
    private var sound: String? = null

    @SerializedName("body")
    @Expose
    private var body: String? = null

    fun getBadge(): Int? {
        return badge
    }

    fun setBadge(badge: Int?) {
        this.badge = badge
    }

    fun getSound(): String? {
        return sound
    }

    fun setSound(sound: String?) {
        this.sound = sound
    }

    fun getBody(): String? {
        return body
    }

    fun setBody(body: String?) {
        this.body = body
    }

    override fun toString(): String {
        return "{notification: {body: $body}}"
    }
}