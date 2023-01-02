package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.List;

public class BillInfo implements Serializable {
    private String idBill;
    private List<Cart> listCart;

    public String getIdBill() {
        return idBill;
    }

    public void setIdBill(String idBill) {
        this.idBill = idBill;
    }

    public List<Cart> getListCart() {
        return listCart;
    }

    public void setListCart(List<Cart> listCart) {
        this.listCart = listCart;
    }

    public BillInfo(String idBill, List<Cart> listCart) {
        this.idBill = idBill;
        this.listCart = listCart;
    }
}
