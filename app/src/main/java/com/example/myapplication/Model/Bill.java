package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Bill  {
    private String ID;
    private float totalPrice;
    private String dateTime;
    private List<Cart> listCart;

    public Bill() {
    }


    public Bill(String ID, float totalPrice, String dateTime,List<Cart> listCart) {
        this.ID = ID;
        this.totalPrice = totalPrice;
        this.dateTime = dateTime;
        this.listCart = listCart;
    }

    public List<Cart> getListCart() {
        return listCart;
    }

    public void setListCart(List<Cart> listCart) {
        this.listCart = listCart;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
