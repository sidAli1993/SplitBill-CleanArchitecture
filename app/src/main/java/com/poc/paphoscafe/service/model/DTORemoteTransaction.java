package com.poc.paphoscafe.service.model;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DTORemoteTransaction implements Serializable {
    @SerializedName("trans_id")
    @Expose
    @NonNull
    String transId;

    @SerializedName("price")
    @Expose
    String price;

    @NonNull
    public String getTransId() {
        return transId;
    }

    public void setTransId(@NonNull String transId) {
        this.transId = transId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
