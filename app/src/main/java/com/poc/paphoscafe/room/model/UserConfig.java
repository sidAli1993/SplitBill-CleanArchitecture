package com.poc.paphoscafe.room.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user_config")
public class UserConfig {
    @ColumnInfo
    @PrimaryKey(autoGenerate = true)
    int id;

    @ColumnInfo(name = "pushy_token")
    private String pushyToken;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPushyToken() {
        return pushyToken;
    }

    public void setPushyToken(String pushyToken) {
        this.pushyToken = pushyToken;
    }
}
