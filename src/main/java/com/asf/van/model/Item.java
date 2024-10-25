package com.asf.van.model;

public class Item {

    private long itemId;
    private long itemCode;
    private String description;
    private String quantity;
    private String uomCode;
    private String userName;
    private long headerId;
    private float price;
    private String focFlag;
    private String quantityOnHand;
    private String invoiceNum;
    private String location;
    private String receiptNum;
    private String foc;
    private String vehicleNo;
    private String orderCategory;
    private long orderNumber;
    // Dinesh VAT
    private float perVATRate;
    private float itemAmount;
    private float vatAmount;

    private boolean status;
    private long priceCnt;

    public String getOrderCategory() {
        return orderCategory;
    }

    public void setOrderCategory(String orderCategory) {
        this.orderCategory = orderCategory;
    }

    public String getReceiptNum() {
        return this.receiptNum;
    }

    public void setReceiptNum(String receiptNum) {
        this.receiptNum = receiptNum;
    }

    public String getLocation() {
        return this.location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getInvoiceNum() {
        return this.invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getQuantityOnHand() {
        return this.quantityOnHand;
    }

    public void setQuantityOnHand(String quantityOnHand) {
        this.quantityOnHand = quantityOnHand;
    }

    public String getFocFlag() {
        return this.focFlag;
    }

    public void setFocFlag(String focFlag) {
        this.focFlag = focFlag;
    }

    public long getItemId() {
        return this.itemId;
    }

    public void setItemId(long itemId) {
        this.itemId = itemId;
    }

    public long getItemCode() {
        return this.itemCode;
    }

    public void setItemCode(long itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getQuantity() {
        return this.quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getUomCode() {
        return this.uomCode;
    }

    public void setUomCode(String uomCode) {
        this.uomCode = uomCode;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getHeaderId() {
        return this.headerId;
    }

    public void setHeaderId(long headerId) {
        this.headerId = headerId;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String toString() {
        return "Item [itemId=" + this.itemId + ", itemCode=" + this.itemCode + ", description=" + this.description + ", quantity=" + this.quantity + ", uomCode=" + this.uomCode + ", vehicleNo=" + this.vehicleNo + ",foc=" + this.foc + "]";
    }

    public void setFoc(String foc) {
        this.foc = foc;
    }

    public String getFoc() {
        return foc;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public float getPerVATRate() {
        return perVATRate;
    }

    public void setPerVATRate(float perVATRate) {
        this.perVATRate = perVATRate;
    }

    public float getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(float itemAmount) {
        this.itemAmount = itemAmount;
    }

    public float getVatAmount() {
        return vatAmount;
    }

    public void setVatAmount(float vatAmount) {
        this.vatAmount = vatAmount;
    }

    public long getPriceCnt() {
        return priceCnt;
    }

    public void setPriceCnt(long priceCnt) {
        this.priceCnt = priceCnt;
    }
}
