package com.example.lipuhossain.productivitymanager.models;

/**
 * Created by lipuhossain on 10/1/16.
 */

public class Schedule {
    private String date = null;
    private String id = null;
    private String in_time = null;
    private String out_time = null;
    private String break_time = null;
    private String total_treatment_time = null;
    private String target_treatment_time = null;
    private String actual_treatment_time = null;
    private String target_productivity = null;
    private String actual_productivity = null;
    private String target_clockout_time = null;
    private String actual_clockout_time = null;
    private String schedule_no = null;

    public String getSchedule_no() {
        return schedule_no;
    }

    public void setSchedule_no(String schedule_no) {
        this.schedule_no = schedule_no;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIn_time() {
        return in_time;
    }

    public void setIn_time(String in_time) {
        this.in_time = in_time;
    }

    public String getActual_clockout_time() {
        return actual_clockout_time;
    }

    public void setActual_clockout_time(String actual_clockout_time) {
        this.actual_clockout_time = actual_clockout_time;
    }

    public String getTarget_clockout_time() {
        return target_clockout_time;
    }

    public void setTarget_clockout_time(String target_clockout_time) {
        this.target_clockout_time = target_clockout_time;
    }

    public String getActual_productivity() {
        return actual_productivity;
    }

    public void setActual_productivity(String actual_productivity) {
        this.actual_productivity = actual_productivity;
    }

    public String getTarget_productivity() {
        return target_productivity;
    }

    public void setTarget_productivity(String target_productivity) {
        this.target_productivity = target_productivity;
    }

    public String getActual_treatment_time() {
        return actual_treatment_time;
    }

    public void setActual_treatment_time(String actual_treatment_time) {
        this.actual_treatment_time = actual_treatment_time;
    }

    public String getTarget_treatment_time() {
        return target_treatment_time;
    }

    public void setTarget_treatment_time(String target_treatment_time) {
        this.target_treatment_time = target_treatment_time;
    }

    public String getTotal_treatment_time() {
        return total_treatment_time;
    }

    public void setTotal_treatment_time(String total_treatment_time) {
        this.total_treatment_time = total_treatment_time;
    }

    public String getBreak_time() {
        return break_time;
    }

    public void setBreak_time(String break_time) {
        this.break_time = break_time;
    }

    public String getOut_time() {
        return out_time;
    }

    public void setOut_time(String out_time) {
        this.out_time = out_time;
    }

    public Schedule() {
    }
}
