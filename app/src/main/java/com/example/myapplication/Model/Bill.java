package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

public class Bill implements Parcelable {
    private String ID;
    private String DrinkName;
    private float Price;
    private int quantity;
    private float totalPrice;

    public Bill() {
    }

    public Bill(String ID, String drinkName, float price, int quantity, float totalPrice) {
        this.ID = ID;
        DrinkName = drinkName;
        Price = price;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    protected Bill(Parcel in){
        ID = in.readString();
        DrinkName = in.readString();
        Price = in.readFloat();
        quantity = in.readInt();
        totalPrice = in.readFloat();
    }

    public static final Creator<Bill> CREATOR = new Creator<Bill>() {
        @Override
        public Bill createFromParcel(Parcel in) {
            return new Bill(in);
        }

        @Override
        public Bill[] newArray(int size) {
            return new Bill[size];
        }
    };

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public float getPrice() {
        return Price;
    }

    public void setPrice(float price) {
        Price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(float totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(ID);
        parcel.writeString(DrinkName);
        parcel.writeFloat(Price);
        parcel.writeInt(quantity);
        parcel.writeFloat(totalPrice);
    }
}
