package com.example.myapplication.Model;

import java.util.Date;

public class Bill {
    private int Billid;
    private int CustomerID;
    private int StaffID;
    private int TableID;
    private float totalPrice;
    private Date DateTime;

    public Bill() {
    }

    public Bill(int billid, int customerID, int staffID, int tableID, float totalPrice, Date dateTime) {
        Billid = billid;
        CustomerID = customerID;
        StaffID = staffID;
        TableID = tableID;
        this.totalPrice = totalPrice;
        DateTime = dateTime;
    }

    public int getBillid() {
        return Billid;
    }

    public void setBillid(int billid) {
        Billid = billid;
    }

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public int getStaffID() {
        return StaffID;
    }

    public void setStaffID(int staffID) {
        StaffID = staffID;
    }

    public int getTableID() {
        return TableID;
    }

    public void setTableID(int tableID) {
        TableID = tableID;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getDateTime() {
        return DateTime;
    }

    public void setDateTime(Date dateTime) {
        DateTime = dateTime;
    }
}
