package com.example.myapplication.Listener;

import com.example.myapplication.Model.Cart;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<Cart> cartModelList);
    void onCartLoadFail(String message);
}
