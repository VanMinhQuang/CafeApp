package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Bill  {
    private String ID;
    private float totalPrice;
    private String date;
    private String time;
    private List<Cart> listCart;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Bill() {
    }


    public Bill(String ID, float totalPrice, String date,String time,List<Cart> listCart) {
        this.ID = ID;
        this.totalPrice = totalPrice;
        this.date = date;
        this.time = time;
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

}
