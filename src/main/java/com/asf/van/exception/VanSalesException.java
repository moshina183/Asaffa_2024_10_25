package com.asf.van.exception;

public class VanSalesException extends Exception {
    
    private static final long serialVersionUID = 5492885359382476876L;
    private String message;

    @Override
    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public VanSalesException() {
    }

    public VanSalesException(String message) {
        super(message);
        this.message = message;
    }
}
