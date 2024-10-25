package com.asf.van.model;

public class User {

    private boolean isValid;
    private long userId;
    private String userName;
    private long organizationId;
    private String salesManName;
    private String vehicleNo;

    public String getVehicleNo() {
        return this.vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getSalesManName() {
        return this.salesManName;
    }

    public void setSalesManName(String salesManName) {
        this.salesManName = salesManName;
    }

    public boolean isValid() {
        return this.isValid;
    }

    public void setValid(boolean isValid) {
        this.isValid = isValid;
    }

    public long getUserId() {
        return this.userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "User{" + "isValid=" + isValid + ", userId=" + userId + ", userName=" + userName + ", organizationId=" + organizationId + ", salesManName=" + salesManName + ", vehicleNo=" + vehicleNo + '}';
    }

    public long getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(long organizationId) {
        this.organizationId = organizationId;
    }

    public String getUserName() {
        return this.userName;
    }

}
