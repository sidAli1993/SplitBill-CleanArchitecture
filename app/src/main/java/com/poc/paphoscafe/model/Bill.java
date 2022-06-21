package com.poc.paphoscafe.model;

import java.io.Serializable;

public class Bill implements Serializable {
    private long billAmount;
    private long totalAmount;
    private float mealRating;
    private float serviceRating;
    private int tipPercent;
    private long tipAmount;
    private TransactionMode transactionMode;
    private String cardNumber;
    private String pId;
    private String transactionId;
    private boolean isFlag=true;

    public long getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(long billAmount) {
        this.billAmount = billAmount;
        if (!isFlag()){
            updateValues();
        }
        updateTotalAmount();
    }

    public float getMealRating() {
        return mealRating;
    }

    public void setMealRating(float mealRating) {
        this.mealRating = mealRating;
    }

    public float getServiceRating() {
        return serviceRating;
    }

    public void setServiceRating(float serviceRating) {
        this.serviceRating = serviceRating;
    }

    public int getTipPercent() {
        return tipPercent;
    }

    public void setTipPercent(int tipPercent) {
        this.tipPercent = tipPercent;
        if (!isFlag()){
            setTipAmount((billAmount / 100) * tipPercent);
        }
    }

    public long getTipAmount() {
        return tipAmount;
    }

    public void setTipAmount(long tipAmount) {
        this.tipAmount = tipAmount;
        updateTotalAmount();
    }
    public void updateValues(){
        setTipAmount((billAmount / 100) * tipPercent);
    }
    public TransactionMode getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(TransactionMode transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public long getTotalAmount() {
        updateTotalAmount();
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
    }

    private void updateTotalAmount() {
        setTotalAmount(billAmount + tipAmount);
    }

    public String getpId() {
        return pId;
    }

    public void setpId(String pId) {
        this.pId = pId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
    public boolean isFlag() {
        return isFlag;
    }

    public void setFlag(boolean flag) {
        isFlag = flag;
    }
}
