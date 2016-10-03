package com.example.lipuhossain.productivitymanager.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.customViews.SCCustomDialog;
import com.example.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import com.example.lipuhossain.productivitymanager.models.CustomDate;
import com.example.lipuhossain.productivitymanager.models.Schedule;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

/**
 * Created by lipuhossain on 10/1/16.
 */

public class GlobalUtils {

    public static String TARGET_PRODUCTIVITY = "88%";
    public static String TOTAL_TREATMENT_TIME = "417";
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

        String date = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) +ampm;

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
}
