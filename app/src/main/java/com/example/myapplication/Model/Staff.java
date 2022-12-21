package com.example.myapplication.Model;

public class Staff {
    private int id;
    private String password;
    private String username;
    private String address;
    private String phoneNumber;
    private int Certificate;
    private String position;

    public Staff() {
    }

    public Staff(int id, String username, String password, String address, String phoneNumber, int certificate, String position) {
        this.id = id;
        this.password = password;
        this.username = username;
        this.address = address;
        this.phoneNumber = phoneNumber;
        Certificate = certificate;
        this.position = position;
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

    public int getCertificate() {
        return Certificate;
    }

    public void setCertificate(int certificate) {
        Certificate = certificate;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
