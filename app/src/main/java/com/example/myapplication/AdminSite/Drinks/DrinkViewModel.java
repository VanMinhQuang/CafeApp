package com.example.myapplication.AdminSite.Drinks;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DrinkViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DrinkViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is drink model");
    }

    public LiveData<String> getText() {
        return mText;
    }
}