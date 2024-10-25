package com.asf.van.model;

import java.util.List;

public class VehicleDetails {

    private String customerName;
    private String transactionType;
    private String customerSiteId;
    private String customerSite;
    private String paymentMethod;
    private String userName;
    private int orgId;
    private String custAccountId;
    private String transactionTypeId;
    private String subInvCode;
    private String transType;
    private float totalAmount;
    private String invoiceNum;
    private String locationId;
    private String rentalKnockOff;
    private String referenceNo;
    

    private List<Item> items;
    // vat fields
    private int vatRate;
    private float vatAmount;
    private float netAmount;

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getRentalKnockOff() {
        return this.rentalKnockOff;
    }

    public void setRentalKnockOff(String rentalKnockOff) {
        this.rentalKnockOff = rentalKnockOff;
    }

    public String getInvoiceNum() {
        return this.invoiceNum;
    }

    public String getLocationId() {
        return this.locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public float getTotalAmount() {
        return this.totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCustomerSiteId() {
        return this.customerSiteId;
    }

    public void setCustomerSiteId(String customerSiteId) {
        this.customerSiteId = customerSiteId;
    }

    public String getTransType() {
        return this.transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getSubInvCode() {
        return this.subInvCode;
    }

    public void setSubInvCode(String subInvCode) {
        this.subInvCode = subInvCode;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getTransactionType() {
        return this.transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCustomerSite() {
        return this.customerSite;
    }

    public void setCustomerSite(String customerSite) {
        this.customerSite = customerSite;
    }

    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getOrgId() {
        return this.orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getCustAccountId() {
        return this.custAccountId;
    }

    public void setCustAccountId(String custAccountId) {
        this.custAccountId = custAccountId;
    }

    public String getTransactionTypeId() {
        return this.transactionTypeId;
    }

    public void setTransactionTypeId(String transactionTypeId) {
        this.transactionTypeId = transactionTypeId;
    }

    public List<Item> getItems() {
        return this.items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
    // - VAT Getter/Setter 
    public int getVatRate() {
        return vatRate;
    }

    public void setVatRate(int vatRate) {
        this.vatRate = vatRate;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public float getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(float netAmount) {
        this.netAmount = netAmount;
    }
}
