package com.example.myapplication.Model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Schedule implements Serializable {
    public String date;
    public String shift;
    public String staffBarista;
    public String staffWaiter;
    public String staffGuard;
    public String scheduleID;

    public Schedule() {
    }

    public Schedule(String scheduleID, String date, String shift, String staffBarista, String staffWaiter, String staffGuard) {
        this.scheduleID = scheduleID;
        this.date = date;
        this.shift = shift;
        this.staffBarista = staffBarista;
        this.staffWaiter = staffWaiter;
        this.staffGuard = staffGuard;
    }
    public Map<String, Object> toMap(){
        HashMap<String, Object> result = new HashMap<>();
        result.put("date", date);
        result.put("shift", shift);
        result.put("staffBarista", staffBarista);
        result.put("staffWaiter",staffWaiter);
        result.put("staffGuard",staffGuard);
        return result;
    }

    public String getScheduleID(){return scheduleID;}

    public void setScheduleID(String scheduleID){this.scheduleID = scheduleID;}

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getShift() {
        return shift;
    }

    public void setShift(String shift) {
        this.shift = shift;
    }

    public String getStaffBarista() {
        return staffBarista;
    }

    public void setStaffBarista(String staffBarista) {
        this.staffBarista = staffBarista;
    }

    public String getStaffWaiter() {
        return staffWaiter;
    }

    public void setStaffWaiter(String staffWaiter) {
        this.staffWaiter = staffWaiter;
    }

    public String getStaffGuard() {
        return staffGuard;
    }

    public void setStaffGuard(String staffGuard) {
        this.staffGuard = staffGuard;
    }
}
