package com.example.myapplication.Model;

public class Notification {
    String id;
    String name;
    String text;
    String dateTime;

    public Notification() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Notification(String id, String name, String text, String dateTime) {
        this.id = id;
        this.name = name;
        this.text = text;
        this.dateTime = dateTime;
    }
}
