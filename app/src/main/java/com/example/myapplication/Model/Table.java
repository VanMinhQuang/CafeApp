package com.example.myapplication.Model;

import java.util.HashMap;
import java.util.Map;

public class Table {

    private String tableID;
    private String status;

    public Table() {
    }

    public Table(String tableID, String status) {
        this.tableID = tableID;
        this.status = status;
    }

    public Map<String,Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("tableID", tableID);
        result.put("status", status);
        return result;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
