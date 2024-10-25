package com.asf.van.model;

public class Organization {

    private int organizationId;
    private int organizationCode;
    private String organizationName;

    public int getOrganizationId() {
        return this.organizationId;
    }

    public void setOrganizationId(int organizationId) {
        this.organizationId = organizationId;
    }

    public int getOrganizationCode() {
        return this.organizationCode;
    }

    public void setOrganizationCode(int organizationCode) {
        this.organizationCode = organizationCode;
    }

    public String getOrganizationName() {
        return this.organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
