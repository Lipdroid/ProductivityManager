package com.example.lipuhossain.productivitymanager.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.customViews.SCCustomDialog;
import com.example.lipuhossain.productivitymanager.interfaces.DialogForValueCallback;
import com.example.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import com.example.lipuhossain.productivitymanager.models.CustomDate;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.models.Time;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 * Created by lipuhossain on 10/1/16.
 */

public class GlobalUtils {

    public static String TARGET_PRODUCTIVITY = "";
    public static String TOTAL_TREATMENT_TIME = "0";
    public static  boolean no_counting = true;
    public static String CLOCK_IN = "Clock In";
    public static String CLOCK_OUT = "Clock Out";
    public static String SESSION_ENDED = "Session ended";
    public static Schedule calculated_schedule = new Schedule();




    public static String convertTimeInMinutes(String hours,String minutes){
        return Integer.toString((Integer.parseInt(hours)*60)+Integer.parseInt(minutes));
    }


    public static ArrayList<CustomDate> getDates() {
        ArrayList<CustomDate> dates = new ArrayList<CustomDate>();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        for (int index = 1; index < 365; index++) {

            CustomDate date = new CustomDate();
            date.setDate("" + calendar.get(Calendar.DATE));
            date.setDay(getDay(calendar.get(Calendar.DAY_OF_WEEK)));
            date.setYear("" + calendar.get(Calendar.YEAR));
            date.setMonth("" + getMonth(calendar.get(Calendar.MONTH)));

            date.setFormattedDate(calendar.get(Calendar.YEAR) + "-"
                    + (calendar.get(Calendar.MONTH) + 1) + "-"
                    + calendar.get(Calendar.DATE));

            dates.add(date);

            calendar.add(Calendar.DATE, 1);

        }
        return dates;
    }


    public static void showConfirmDialog(Context context, String title, String body, String action1, String action2, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_confirm_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnCancel = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {

                tvBody.setText(body);

        }

        if (action1 != null) {
            btnOK.setText(action1);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        if (action2 != null) {
            btnCancel.setText(action2);
        }
        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }


    public static CustomDate getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        CustomDate date = new CustomDate();
        date.setDate("" + calendar.get(Calendar.DATE));
        date.setDay(getDay(calendar.get(Calendar.DAY_OF_WEEK)));
        date.setYear("" + calendar.get(Calendar.YEAR));
        date.setMonth("" + getMonth(calendar.get(Calendar.MONTH)));

        date.setFormattedDate(calendar.get(Calendar.YEAR) + "-"
                + (calendar.get(Calendar.MONTH) + 1) + "-"
                + calendar.get(Calendar.DATE));

        return date;

    }


    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String ampm = DateUtils.getAMPMString(calendar.get(Calendar.AM_PM));

        String date = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) +" "+ampm;

        return date;

    }

    private static String getDay(int index) {
        switch (index) {
            case Calendar.SUNDAY:
                return "SUN";
            case Calendar.MONDAY:
                return "MON";
            case Calendar.TUESDAY:
                return "TUE";
            case Calendar.WEDNESDAY:
                return "WED";
            case Calendar.THURSDAY:
                return "THUR";
            case Calendar.FRIDAY:
                return "FRI";
            case Calendar.SATURDAY:
                return "SAT";
        }
        return "";
    }

    private static String getMonth(int index) {
        switch (index) {
            case Calendar.JANUARY:
                return "JANUARY";
            case Calendar.FEBRUARY:
                return "FEBRUARY";
            case Calendar.MARCH:
                return "MARCH";
            case Calendar.APRIL:
                return "APRIL";
            case Calendar.MAY:
                return "MAY";
            case Calendar.JUNE:
                return "JUNE";
            case Calendar.JULY:
                return "JULY";
            case Calendar.AUGUST:
                return "AUGUST";
            case Calendar.SEPTEMBER:
                return "SEPTEMBER";
            case Calendar.OCTOBER:
                return "OCTOBER";
            case Calendar.NOVEMBER:
                return "NOVEMBER";
            case Calendar.DECEMBER:
                return "DECEMBER";
        }
        return "";
    }

    public static void showProgress(final RingProgressBar progress, final int value){
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = 0;

                while(jumpTime < value) {
                    try {
                        sleep(35);
                        jumpTime += 1;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();

    }


    public static void showInfoDialog(Context context, String title, String body, String action, final SCDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.layout_show_info_dialog, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        TextView tvTitle = (TextView) infoDialog.findViewById(R.id.dialog_tv_title);
        TextView tvBody = (TextView) infoDialog.findViewById(R.id.dialog_tv_body);

        if (title == null) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }

        if (body == null) {
            tvBody.setVisibility(View.GONE);
        } else {
            tvBody.setText(body);
        }

        if (action != null) {
            btnOK.setText(action);
        }
        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction1();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static void resetProgress(final RingProgressBar progress) {
        final int value = progress.getProgress();
        final Thread t = new Thread() {
            @Override
            public void run() {
                int jumpTime = value;

                while(jumpTime != 0) {
                    try {
                        sleep(35);
                        jumpTime -= 1;
                        progress.setProgress(jumpTime);
                    }
                    catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }
    //Calculate productivity
    public static String get_productivity(String total_treatment_hours,String actual_treatment_hours){
        int productivity = ((Integer.parseInt(total_treatment_hours)/Integer.parseInt(actual_treatment_hours))*100);
        return productivity+"";
    }

    //Calculate how long have to stay in the office
    public static String get_target_treatment_hours(String productivity,String total_treatment_hours){
        int  actual_treatment_hours = ((Integer.parseInt(total_treatment_hours)/Integer.parseInt(productivity))*100);
        return actual_treatment_hours+"";
    }

    //convert String into time
    public static Date convert_string_time_into_original_time(String hhmmaa){
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm aa");
        Date convertedDate = new Date();
        try {
            convertedDate = dateFormat.parse(hhmmaa);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return convertedDate;
    }

    //convert minutes into hour and minute
    public static String convert_minutes_to_hours(String minutess){
        int hours = Integer.parseInt(minutess) / 60; //since both are ints, you get an int
        int minutes = Integer.parseInt(minutess) % 60;
        System.out.printf("%d:%02d", hours, minutes);
        return hours+":"+minutes;
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static void printDifference(Date startDate, Date endDate){

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

    }

    public static String add_hours_to_time(String starting,String add){
         Date date = convert_string_time_into_original_time(starting);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, Integer.parseInt(add));
        String ampm = DateUtils.getAMPMString(calendar.get(Calendar.AM_PM));
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        if(hours >= 12){
            if (hours != 12)
                hours -= 12;
        }else{
            if (hours == 0)
                hours += 12;
        }
        return  hours+":"+minutes+" "+ampm;
    }

    //Dialog implementation

    public static void showDialogToGetUserInput(final Context context, final DialogForValueCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_user_input, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnOK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnCancel = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);

        final EditText productivity = (EditText) infoDialog.findViewById(R.id.et_productivity);
        final EditText hours = (EditText) infoDialog.findViewById(R.id.et_hour);
        final EditText minutes = (EditText) infoDialog.findViewById(R.id.et_minute);


        btnOK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    String productivity_txt = "";
                    String hours_txt = "";
                    String minutes_txt = "00";
                    if(!productivity.getText().toString().isEmpty()){
                        productivity_txt = productivity.getText().toString().trim();
                    }else{
                        //show error
                        showInfoDialog(context, "Error", "Please type your productivit for today.", "OK",null);
                        return;
                    }
                    if(!hours.getText().toString().isEmpty()){
                        hours_txt = hours.getText().toString().trim();
                    }else{
                        showInfoDialog(context, "Error", "Please type your total hour for today.", "OK",null);
                        return;
                    }
                    if(!minutes.getText().toString().isEmpty()){
                        minutes_txt = minutes.getText().toString().trim();
                    }
                    dialogCallback.onAction1(productivity_txt,hours_txt,minutes_txt);
                }
                infoDialog.dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }


    public static Time get_time(String time){
       Time t = new Time();
        String[] separated = time.split(":");
        String hours = separated[0]; // this will contain "hour"
        String[] splitStr = separated[1].trim().split("\\s+");
        String minutes = splitStr[0];
        String am_pm = splitStr[1];

        t.setAm_pm(am_pm);
        t.setHours(Integer.parseInt(hours));
        t.setMinutes(Integer.parseInt(minutes));
        t.setTime_in_string(time);

        return t;
    }

    public static String get_target_clockout(Time intime, String add_minutes) {
        String clockput = "";
        int minutes = Integer.parseInt(add_minutes);
        int hours = minutes/60;
        int minute = minutes%60;
        String ampm ="";
        int updated_hour = 0;
        int updated_minute = 0;

        if(intime.getAm_pm().equals("AM")){
             updated_hour = intime.getHours()+hours;
             updated_minute = intime.getMinutes()+minute;
            if (updated_minute >= 60) {
                updated_hour += updated_minute/60;
                updated_minute = updated_minute%60;
            }


            if(updated_hour >= 12){
                if(updated_hour >12)
                        updated_hour -= 12;
                ampm = "PM";
            }else{
                ampm = "AM";
            }

        }else {
             updated_hour = intime.getHours()+hours;
             updated_minute = intime.getMinutes()+minute;

            if (updated_minute >= 60) {
                updated_hour += updated_minute/60;
                updated_minute = updated_minute%60;
            }


            if(updated_hour >=12){
                if(updated_hour > 12)
                    updated_hour -= 12;
                ampm = "AM";
            }else{
                ampm = "PM";
            }
        }
        clockput = updated_hour+":"+updated_minute+" "+ampm;

        return clockput;

    }


    public static Time get_actual_working_hours(Time intime, Time outtime) {

        Time t = new Time();
        if(intime.getAm_pm().equals("AM") && outtime.getAm_pm().equals("AM") || intime.getAm_pm().equals("PM") && outtime.getAm_pm().equals("PM")){
            int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours()+"",outtime.getMinutes()+"")) - Integer.parseInt(convertTimeInMinutes(intime.getHours()+"",intime.getMinutes()+"")));
            t.setHours(total_minute/60);
            t.setMinutes(total_minute%60);

        }else{
            int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours()+"",outtime.getMinutes()+"")) - Integer.parseInt(convertTimeInMinutes(intime.getHours()+"",intime.getMinutes()+"")));
            total_minute += 720;
            t.setHours(total_minute/60);
            t.setMinutes(total_minute%60);
        }

        return  t;

    }

    public static Time get_break(Time intime, Time outtime) {

        Time t = new Time();
        if(intime.getAm_pm().equals("AM") && outtime.getAm_pm().equals("AM") || intime.getAm_pm().equals("PM") && outtime.getAm_pm().equals("PM")){
            int total_minute = (Integer.parseInt(convertTimeInMinutes(intime.getHours()+"",intime.getMinutes()+"")) - Integer.parseInt(convertTimeInMinutes(outtime.getHours()+"",outtime.getMinutes()+"")));
            t.setHours(total_minute/60);
            t.setMinutes(total_minute%60);

        }else{
            int total_minute = (Integer.parseInt(convertTimeInMinutes(intime.getHours()+"",intime.getMinutes()+"")) - Integer.parseInt(convertTimeInMinutes(outtime.getHours()+"",outtime.getMinutes()+"")));
            total_minute += 720;
            t.setHours(total_minute/60);
            t.setMinutes(total_minute%60);
        }

        return  t;

    }
}
