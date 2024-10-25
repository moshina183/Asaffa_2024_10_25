package com.asf.van.model;

public class TransactionType {

    private long transactionId;
    private String transactionName;

    public long getTransactionId() {
        return this.transactionId;
    }

    public void setTransactionId(long transactionId) {
        this.transactionId = transactionId;
    }

    public String getTransactionName() {
        return this.transactionName;
    }

    public void setTransactionName(String transactionName) {
        this.transactionName = transactionName;
    }
}
