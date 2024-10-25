package com.asf.van.model;

public class Customer {

    private long customerId;
    private String customerName;
    private String partyId;

    public long getCustomerId() {
        return this.customerId;
    }

    public String getPartyId() {
        return this.partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public void setCustomerId(long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
