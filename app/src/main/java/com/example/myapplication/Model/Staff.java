package com.example.myapplication.Model;

import java.util.HashMap;
import java.util.Map;

public class Staff {
    private int id;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;
    private String DisplayName;
    private String position;
    private String imageURI;

    public Staff() {
    }

    public Staff(int id, String username, String password, String address, String phoneNumber, String DisplayName, String position, String imageURI) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.DisplayName = DisplayName;
        this.position = position;
        this.imageURI = imageURI;
    }

    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("username", username);
        result.put("password", password);
        result.put("address", address);
        result.put("phoneNumber", phoneNumber);
        result.put("DisplayName", DisplayName);
        result.put("position", position);
        return  result;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return DisplayName;
    }

    public void setDisplayName(String DisplayName) {
        this.DisplayName = DisplayName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }
}
