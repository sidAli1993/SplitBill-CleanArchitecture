package com.poc.paphoscafe.model;

import java.util.ArrayList;

public class Split {
    private int numberOfSplits = 1;
    private int current = 1;
    private long perPersonShare;
    private long totalAmount;
    private long unpaidAmount;
    private long paidAmount;
    private String pid;
    private String transactionId;
    private ArrayList<Bill> processedTransactions;

    public int getNumberOfSplits() {
        return numberOfSplits;
    }

    public void setNumberOfSplits(int numberOfSplits) {
        this.numberOfSplits = numberOfSplits;
        manipulateValues();
    }

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }

    public long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(long totalAmount) {
        this.totalAmount = totalAmount;
        manipulateValues();
    }

    public long getUnpaidAmount() {
        return unpaidAmount;
    }

    public void setUnpaidAmount(long unpaidAmount) {
        this.unpaidAmount = unpaidAmount;
    }

    public long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(long paidAmount) {
        this.paidAmount = paidAmount;
        manipulateValues();
    }

    public ArrayList<Bill> getProcessedTransactions() {
        return processedTransactions;
    }

    public void setProcessedTransactions(ArrayList<Bill> processedTransactions) {
        this.processedTransactions = processedTransactions;
    }

    public void addNewProcessedPayment(Bill bill) {
        if (this.processedTransactions == null) {
            this.processedTransactions = new ArrayList<>();
        }
        this.processedTransactions.add(bill);
        updatePaidAmount();
    }

    public long getPerPersonShare() {
        return perPersonShare;
    }

    public void setPerPersonShare(long perPersonShare) {
        this.perPersonShare = perPersonShare;
    }

    private void manipulateValues() {
        unpaidAmount = totalAmount - paidAmount;
        perPersonShare = totalAmount / numberOfSplits;
    }

    private void updatePaidAmount() {
        long paidAmount = 0;
        for (Bill bill : this.processedTransactions) {
            paidAmount += bill.getBillAmount();
        }

        setPaidAmount(paidAmount);
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
