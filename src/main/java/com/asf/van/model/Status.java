package com.asf.van.model;

public class Status {

    private String invoiceNum;
    private String printInvoiceFlag;
    private boolean result;
    private String orderNumber;
    private String status;
    private String creditBalance;
    private boolean balanceFlag;
    private String paymentMethod;
    
    public String getStatus() {
        return this.status;
    }

    public String getOrderNumber() {
        return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isResult() {
        return this.result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public String getPrintInvoiceFlag() {
        return this.printInvoiceFlag;
    }

    public void setPrintInvoiceFlag(String printInvoiceFlag) {
        this.printInvoiceFlag = printInvoiceFlag;
    }

    public String getInvoiceNum() {
        return this.invoiceNum;
    }

    public void setInvoiceNum(String invoiceNum) {
        this.invoiceNum = invoiceNum;
    }

    public String getCreditBalance() {
        return creditBalance;
    }

    public void setCreditBalance(String creditBalance) {
        this.creditBalance = creditBalance;
    }

    public boolean isBalanceFlag() {
        return balanceFlag;
    }

    public void setBalanceFlag(boolean balanceFlag) {
        this.balanceFlag = balanceFlag;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


}
