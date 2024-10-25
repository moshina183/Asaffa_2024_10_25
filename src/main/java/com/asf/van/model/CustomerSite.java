package com.asf.van.model;

public class CustomerSite {

    private long locationId;
    private String address;
    private String addressLoc;

    public String getAddressLoc() {
        return this.addressLoc;
    }

    public void setAddressLoc(String addressLoc) {
        this.addressLoc = addressLoc;
    }

    public long getLocationId() {
        return this.locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
