package com.example.myapplication.Model;

public class Customer {
    private int customerID;
    private String customerName;
    private String customerPhoneNumber;
    private int customerCertificate;
    private String customerAddress;

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public int getCustomerCertificate() {
        return customerCertificate;
    }

    public void setCustomerCertificate(int customerCertificate) {
        this.customerCertificate = customerCertificate;
    }

    public String getCustomerAddress() {
        return customerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        this.customerAddress = customerAddress;
    }

    public Customer() {
    }

    public Customer(int customerID, String customerName, String customerPhoneNumber, int customerCertificate, String customerAddress) {
        this.customerID = customerID;
        this.customerName = customerName;
        this.customerPhoneNumber = customerPhoneNumber;
        this.customerCertificate = customerCertificate;
        this.customerAddress = customerAddress;
    }
}
