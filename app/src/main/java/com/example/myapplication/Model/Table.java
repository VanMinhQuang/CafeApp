package com.example.myapplication.Model;

public class Table {

    private int tableID;
    private String status;

    public Table() {
    }

    public Table(int tableID, String status) {
        this.tableID = tableID;
        this.status = status;
    }

    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
