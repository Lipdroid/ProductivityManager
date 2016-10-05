package com.example.lipuhossain.productivitymanager.models;

/**
 * Created by lipuhossain on 10/5/16.
 */

public class Time {

    private String time_in_string = null;
    private int hours = 0;
    private int minutes = 0;

    public String getAm_pm() {
        return am_pm;
    }

    public void setAm_pm(String am_pm) {
        this.am_pm = am_pm;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getHours() {
        return hours;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public String getTime_in_string() {
        return time_in_string;
    }

    public void setTime_in_string(String time_in_string) {
        this.time_in_string = time_in_string;
    }

    private String am_pm = null;



}
