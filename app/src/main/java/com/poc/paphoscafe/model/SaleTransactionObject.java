package com.poc.paphoscafe.model;

public class SaleTransactionObject {
    private String amount;
    private String currency;
    private TransactionType request;
    private Environment environment;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public TransactionType getRequest() {
        return request;
    }

    public void setRequest(TransactionType request) {
        this.request = request;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
