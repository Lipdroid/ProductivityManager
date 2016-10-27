package ben.work.lipuhossain.productivitymanager.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import ben.work.lipuhossain.productivitymanager.R;
import ben.work.lipuhossain.productivitymanager.constants.Constants;
import ben.work.lipuhossain.productivitymanager.customViews.SCCustomDialog;
import ben.work.lipuhossain.productivitymanager.interfaces.DialogForValueCallback;
import ben.work.lipuhossain.productivitymanager.interfaces.OptionDialogCallback;
import ben.work.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import ben.work.lipuhossain.productivitymanager.interfaces.SeachDialogCallback;
import ben.work.lipuhossain.productivitymanager.models.CustomDate;
import ben.work.lipuhossain.productivitymanager.models.Schedule;
import ben.work.lipuhossain.productivitymanager.models.Time;

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

    public static String TARGET_TREATMENT_TIME = "0";
    public static String TARGET_PRODUCTIVITY = "";
    public static String TOTAL_TREATMENT_TIME = "0";
    public static boolean no_counting = true;
    public static String CLOCK_IN = "Clock In";
    public static String CLOCK_OUT = "Clock Out";
    public static String SESSION_ENDED = "Session ended";
    public static Schedule calculated_schedule = new Schedule();


    public static SharedPreferences preferences(Context mContext) {
        SharedPreferences prefs = mContext.getSharedPreferences(Constants.PREFS_NAME, mContext.MODE_PRIVATE);
        return prefs;
    }

    public static String convertTimeInMinutes(String hours, String minutes) {
        return Integer.toString((Integer.parseInt(hours) * 60) + Integer.parseInt(minutes));
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


    public static CustomDate getCurrentDate() {
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


    public static String getCurrentTime() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());

        String ampm = DateUtils.getAMPMString(calendar.get(Calendar.AM_PM));

        int hourOfDay = calendar.get(Calendar.HOUR);
        if (hourOfDay >= 12) {
            if (hourOfDay != 12)
                hourOfDay -= 12;
        } else {
            if (hourOfDay == 0)
                hourOfDay += 12;
        }

        String date = String.format("%02d", hourOfDay)
                + ":"
                + String.format("%02d", calendar.get(Calendar.MINUTE))
                + " "
                + ampm;

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

    public static void showProgress(final RingProgressBar progress, final int value) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                //if (value <= 400) {
                    int jumpTime = 0;
                    if(progress.getProgress() < value && value<100){
                         jumpTime = progress.getProgress();
                    }

                    while (jumpTime < value) {
                        try {
                            sleep(10);
                            jumpTime += 1;
                            progress.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
              //  }
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
                if (value <= 100){
                    while (jumpTime != 0) {
                        try {
                            sleep(10);
                            jumpTime -= 1;
                            progress.setProgress(jumpTime);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
            }else{
                    progress.setProgress(0);
                }
            }
        };
        t.start();
    }

    //Calculate productivity
    public static String get_productivity(String total_treatment_hours, String actual_treatment_hours) {
        Double productivity = 0.0;
        try {
            productivity = ((Double.parseDouble(total_treatment_hours) / Double.parseDouble(actual_treatment_hours)) * 100);
            //int value = productivity.intValue();
            return String.format("%.2f",productivity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return productivity + "";


    }

    //Calculate how long have to stay in the office
    public static String get_target_treatment_hours(String productivity, String total_treatment_hours) {
        Double actual_treatment_hours = ((Double.parseDouble(total_treatment_hours) / Double.parseDouble(productivity)) * 100);
        int value = actual_treatment_hours.intValue();
        return value + "";
    }

    //convert String into time
    public static Date convert_string_time_into_original_time(String hhmmaa) {
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
    public static String convert_minutes_to_hours(String minutess) {
        int hours = Integer.parseInt(minutess) / 60; //since both are ints, you get an int
        int minutes = Integer.parseInt(minutess) % 60;
        System.out.printf("%d:%02d", hours, minutes);
        return hours + ":" + minutes;
    }


    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public static void printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
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

    public static String add_hours_to_time(String starting, String add) {
        Date date = convert_string_time_into_original_time(starting);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MINUTE, Integer.parseInt(add));
        String ampm = DateUtils.getAMPMString(calendar.get(Calendar.AM_PM));
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        if (hours >= 12) {
            if (hours != 12)
                hours -= 12;
        } else {
            if (hours == 0)
                hours += 12;
        }
        return hours + ":" + minutes + " " + ampm;
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
                    if (!productivity.getText().toString().isEmpty()) {
                        productivity_txt = productivity.getText().toString().trim();
                    } else {
                        //show error
                        showInfoDialog(context, "Error", "Please input hours otherwise type 0 if zero hours.", "OK", null);
                        return;
                    }
                    if (!hours.getText().toString().isEmpty()) {
                        hours_txt = hours.getText().toString().trim();
                    } else {
                        showInfoDialog(context, "Error", "Please input hours otherwise type 0 if zero hours.", "OK", null);
                        return;
                    }
                    if (!minutes.getText().toString().isEmpty()) {
                        minutes_txt = minutes.getText().toString().trim();
                    }
                    dialogCallback.onAction1(productivity_txt, hours_txt, minutes_txt);
                }


                //remove the focus from the edittexts
                IBinder token = hours.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token, 0 );
                IBinder token2 = minutes.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token2, 0 );

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

                //remove the focus from the edittexts
                IBinder token = hours.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token, 0 );
                IBinder token2 = minutes.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token2, 0 );

                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }


    //Dialog implementation

    public static void showDialogToChangeTotalTime(final Context context, final DialogForValueCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_change_total_target, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button btnADD = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnSUB = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);
        Button btnDismiss = (Button) infoDialog.findViewById(R.id.dialog_btn_cancel);


        final EditText hours = (EditText) infoDialog.findViewById(R.id.et_hour);
        final EditText minutes = (EditText) infoDialog.findViewById(R.id.et_minute);


        btnADD.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    String hours_txt = "";
                    String minutes_txt = "00";

                    if (!hours.getText().toString().isEmpty()) {
                        hours_txt = hours.getText().toString().trim();
                    } else {
                        showInfoDialog(context, "Error", "Please input hours otherwise type 0 if zero hours.", "OK", null);
                        return;
                    }
                    if (!minutes.getText().toString().isEmpty()) {
                        minutes_txt = minutes.getText().toString().trim();
                    }
                    dialogCallback.onAction3(0, hours_txt, minutes_txt);
                }

                //remove the focus from the edittexts
                IBinder token = hours.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token, 0 );
                IBinder token2 = minutes.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token2, 0 );
                infoDialog.dismiss();
            }
        });


        btnSUB.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                //your business logic
                if (dialogCallback != null) {
                    String hours_txt = "";
                    String minutes_txt = "00";

                    if (!hours.getText().toString().isEmpty()) {
                        hours_txt = hours.getText().toString().trim();
                    } else {
                        showInfoDialog(context, "Error", "Please input hours otherwise type 0 if zero hours.", "OK", null);
                        return;
                    }
                    if (!minutes.getText().toString().isEmpty()) {
                        minutes_txt = minutes.getText().toString().trim();
                    }
                    dialogCallback.onAction3(1, hours_txt, minutes_txt);
                }

                //remove the focus from the edittexts
                IBinder token = hours.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token, 0 );
                IBinder token2 = minutes.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token2, 0 );
                infoDialog.dismiss();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }

                //remove the focus from the edittexts
                IBinder token = hours.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token, 0 );
                IBinder token2 = minutes.getWindowToken();
                ( (InputMethodManager) context.getSystemService( Context.INPUT_METHOD_SERVICE ) ).hideSoftInputFromWindow( token2, 0 );

                infoDialog.dismiss();

            }
        });


        infoDialog.show();
    }

    public static void showOpdtionDialog(final Context context, final OptionDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_options_menu, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        LinearLayout btnHistory = (LinearLayout) infoDialog.findViewById(R.id.btnHistory);
        LinearLayout btnChangeTime = (LinearLayout) infoDialog.findViewById(R.id.btnChangeTime);
        LinearLayout btnClear = (LinearLayout) infoDialog.findViewById(R.id.btnClear);
        LinearLayout btnSave = (LinearLayout) infoDialog.findViewById(R.id.btnSave);
        LinearLayout btnHelp = (LinearLayout) infoDialog.findViewById(R.id.btnHelp);
        LinearLayout btnCancel = (LinearLayout) infoDialog.findViewById(R.id.btnCancel);


        btnHistory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionHistory();
                }
                infoDialog.dismiss();
            }
        });

        btnClear.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionClear();
                }
                infoDialog.dismiss();
            }
        });

        btnChangeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionChangeTime();
                }
                infoDialog.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionSave();
                }
                infoDialog.dismiss();
            }
        });

        btnHelp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionHelp();
                }
                infoDialog.dismiss();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onActionCancel();
                }
                infoDialog.dismiss();
            }
        });

        infoDialog.show();
    }

    public static void showDialogSearch(final Context context, final ArrayList<String> datelist, final SeachDialogCallback dialogCallback) {
        final SCCustomDialog infoDialog = new SCCustomDialog(context, R.style.CustomDialogTheme);
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.dialog_search_date, null);

        new SCMultipleScreen(context);
        SCMultipleScreen.resizeAllView((ViewGroup) v);

        infoDialog.setContentView(v);

        Button OK = (Button) infoDialog.findViewById(R.id.dialog_btn_positive);
        Button btnDismiss = (Button) infoDialog.findViewById(R.id.dialog_btn_negative);
        Button btnAll = (Button) infoDialog.findViewById(R.id.dialog_btn_all);


        final AutoCompleteTextView date = (AutoCompleteTextView) infoDialog.findViewById(R.id.et_minute);
        String[] dates = datelist.toArray(new String[datelist.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, dates);

        date.setThreshold(1);

        //Set adapter to AutoCompleteTextView
        date.setAdapter(adapter);

        OK.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                String search_txt = "";

                if (dialogCallback != null) {
                    if (!date.getText().toString().isEmpty()) {
                        search_txt = date.getText().toString().trim();
                    } else {
                        showInfoDialog(context, "Error", "Please type your date first", "OK", null);
                        return;
                    }
                    dialogCallback.onAction1(search_txt);
                }
                infoDialog.dismiss();
            }
        });

        btnDismiss.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction2();
                }
                infoDialog.dismiss();
            }
        });

        btnAll.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //your business logic
                if (dialogCallback != null) {
                    dialogCallback.onAction3();
                }
                infoDialog.dismiss();
            }
        });


        infoDialog.show();
    }


    public static Time get_time(String time) {
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
        int hours = minutes / 60;
        int minute = minutes % 60;
        String ampm = "";
        int updated_hour = 0;
        int updated_minute = 0;

        if (intime.getAm_pm().equals("AM")) {
            updated_hour = intime.getHours() + hours;
            updated_minute = intime.getMinutes() + minute;
            if (updated_minute >= 60) {
                updated_hour += updated_minute / 60;
                updated_minute = updated_minute % 60;
            }


            if (updated_hour >= 12) {
                if (updated_hour > 12)
                    updated_hour -= 12;
                if (intime.getHours() == 12)
                    ampm = "AM";
                else
                    ampm = "PM";
            } else {
                ampm = "AM";
            }

        } else {
            updated_hour = intime.getHours() + hours;
            updated_minute = intime.getMinutes() + minute;

            if (updated_minute >= 60) {
                updated_hour += updated_minute / 60;
                updated_minute = updated_minute % 60;
            }


            if (updated_hour >= 12) {
                if (updated_hour > 12)
                    updated_hour -= 12;
                if (intime.getHours() == 12)
                    ampm = "PM";
                else
                    ampm = "AM";
            } else {
                ampm = "PM";
            }
        }
        clockput = (String.format("%02d", updated_hour)
                + ":"
                + String.format("%02d", updated_minute)
                + " "
                + ampm);

        return clockput;

    }


    public static Time get_actual_working_hours(Time intime, Time outtime) {

        Time t = new Time();
        if (intime.getAm_pm().equals("AM") && outtime.getAm_pm().equals("AM") || intime.getAm_pm().equals("PM") && outtime.getAm_pm().equals("PM")) {
            int total_minute = 0;
            if (intime.getHours() == outtime.getHours()) {
                total_minute = (Integer.parseInt(outtime.getMinutes() + "") - Integer.parseInt(intime.getMinutes() + ""));

            } else {
                total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));

                if (intime.getHours() == 12 && outtime.getHours() != 12) {
                    total_minute += 720;
                }
            }
            t.setHours(total_minute / 60);
            t.setMinutes(total_minute % 60);

        } else {
            if ((intime.getHours() == 12 && outtime.getHours() != 12) || (intime.getHours() != 12 && outtime.getHours() == 12)) {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            } else {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                total_minute += 720;
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            }
        }

        return t;

    }

    public static Time get_break(Time intime, Time outtime) {

        Time t = new Time();
        if (intime.getAm_pm().equals("AM") && outtime.getAm_pm().equals("AM") || intime.getAm_pm().equals("PM") && outtime.getAm_pm().equals("PM")) {
            int total_minute = 0;
            if (intime.getHours() == outtime.getHours()) {
                total_minute = (Integer.parseInt(intime.getMinutes() + "") - Integer.parseInt(outtime.getMinutes() + ""));

            } else {
                total_minute = (Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")));

                if (outtime.getHours() == 12 && intime.getHours() != 12) {
                    total_minute += 720;
                }
            }
            t.setHours(total_minute / 60);
            t.setMinutes(total_minute % 60);

        } else {
            if ((intime.getHours() == 12 && outtime.getHours() != 12) || (intime.getHours() != 12 && outtime.getHours() == 12)) {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            } else {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                total_minute += 720;
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            }
        }

        return t;

    }


    public static Time get_worked_in_that_session(Time intime, Time outtime) {

        Time t = new Time();
        if (intime.getAm_pm().equals("AM") && outtime.getAm_pm().equals("AM") || intime.getAm_pm().equals("PM") && outtime.getAm_pm().equals("PM")) {
            int total_minute = 0;
            if (intime.getHours() == outtime.getHours()) {
                total_minute = (Integer.parseInt(outtime.getMinutes() + "") - Integer.parseInt(intime.getMinutes() + ""));

            } else {
                total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));

                if (intime.getHours() == 12 && outtime.getHours() != 12) {
                    total_minute += 720;
                }
            }
            t.setHours(total_minute / 60);
            t.setMinutes(total_minute % 60);

        } else {
            if ((intime.getHours() == 12 && outtime.getHours() != 12) || (intime.getHours() != 12 && outtime.getHours() == 12)) {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            } else {
                int total_minute = (Integer.parseInt(convertTimeInMinutes(outtime.getHours() + "", outtime.getMinutes() + "")) - Integer.parseInt(convertTimeInMinutes(intime.getHours() + "", intime.getMinutes() + "")));
                total_minute += 720;
                t.setHours(total_minute / 60);
                t.setMinutes(total_minute % 60);
            }
        }

        return t;

    }


    public static String convert_time_in_hhmm_format(String hour, String minute) {
        return hour
                + "h"
                + ":"
                + minute
                + "m";
    }

    public static boolean check_conflict(Time check_time, Time from_time) {
        boolean result = false;
        //calculation
        if (from_time.getAm_pm().equals("AM") && check_time.getAm_pm().equals("AM") || from_time.getAm_pm().equals("PM") && check_time.getAm_pm().equals("PM")) {
            //if same am/pm
            if (from_time.getHours() != 12 && check_time.getHours() == 12) {
                if ((from_time.getHours() + 12) < check_time.getHours())
                    return true;

            } else if (from_time.getHours() == 12 && check_time.getHours() != 12) {
                if (from_time.getHours() < (check_time.getHours() + 12))
                    return true;
            } else {
                if (from_time.getHours() == check_time.getHours()) {
                    if (from_time.getMinutes() < check_time.getMinutes()) {
                        return true;
                    }
                } else {
                    if (from_time.getHours() < check_time.getHours()) {
                        return true;
                    }
                }
            }
        } else {
            if (!(from_time.getAm_pm().equals("PM") && check_time.getAm_pm().equals("AM"))) {

                if (check_time.getHours() != 12 && from_time.getHours() != 12) {
                    if ((check_time.getHours() + 12) > from_time.getHours()) {
                        return true;
                    }

                } else if (check_time.getHours() == 12 && from_time.getHours() == 12) {
                    return true;
                } else {
                    if (check_time.getHours() > from_time.getHours()) {
                        return true;
                    }
                }
            }
        }


        return result;
    }
}
