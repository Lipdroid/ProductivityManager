package com.example.lipuhossain.productivitymanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lipuhossain.productivitymanager.MainActivity;
import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.adapters.holder.ScheduleHolder;
import com.example.lipuhossain.productivitymanager.constants.Constants;
import com.example.lipuhossain.productivitymanager.database.DatabaseHelper;
import com.example.lipuhossain.productivitymanager.interfaces.DialogForValueCallback;
import com.example.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.models.Time;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by lipuhossain on 10/1/16.
 */

public class ScheduleAdapter extends BaseAdapter {
    private ArrayList<Schedule> mListData = null;
    private Context mContext = null;
    private DatabaseHelper db = null;
    private ScheduleHolder mHolder = null;
    Schedule schedule = null;
    private View.OnClickListener mOnClickListener = null;


    @Override
    public int getCount() {
        return mListData.size();
    }

    public ScheduleAdapter(Context mContext, ArrayList<Schedule> listData, DatabaseHelper db) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mListData = listData;
        this.db = db;
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.item_schedule, null);

            mHolder = new ScheduleHolder();

            mHolder.time_layout = (LinearLayout) convertView.findViewById(R.id.time);
            mHolder.main_btn = (LinearLayout) convertView.findViewById(R.id.add);
            mHolder.tvIntime = (TextView) convertView.findViewById(R.id.intime);
            mHolder.tvOuttime = (TextView) convertView.findViewById(R.id.outtime);
            mHolder.tvBreaktime = (TextView) convertView.findViewById(R.id.breaktime);
            mHolder.header = (TextView) convertView.findViewById(R.id.header_text);
            mHolder.break_layout = (RelativeLayout) convertView.findViewById(R.id.rl_break);
            mHolder.tv_label_patient_time = (TextView) convertView.findViewById(R.id.tv_label_patient_time);


            mHolder.divider_one = (View) convertView.findViewById(R.id.divider_one);
            mHolder.divider_two = (View) convertView.findViewById(R.id.divider_two);


            convertView.setTag(mHolder);
        } else {
            mHolder = (ScheduleHolder) convertView.getTag();
        }
        Schedule data = mListData.get(position);
        if (position == 0) {
            mHolder.divider_one.setVisibility(View.GONE);
        }

        if (data.getSchedule_no().equals(GlobalUtils.CLOCK_IN)) {
            mHolder.time_layout.setVisibility(View.GONE);
        } else {
            mHolder.time_layout.setVisibility(View.VISIBLE);
        }
        if (data.getWorked_in_that_session() != null && data.getWorked_in_that_session().equals("00:00")) {
            mHolder.break_layout.setVisibility(View.GONE);
            mHolder.divider_two.setVisibility(View.GONE);
        } else if (data.getWorked_in_that_session() != null && !data.getWorked_in_that_session().equals("00:00")) {
            mHolder.break_layout.setVisibility(View.VISIBLE);
            mHolder.divider_two.setVisibility(View.VISIBLE);

            mHolder.tv_label_patient_time.setText("Your Patient Time " + (position + 1));
            Time t = GlobalUtils.get_time(data.getWorked_in_that_session());
            mHolder.tvBreaktime.setText(GlobalUtils.convert_time_in_hhmm_format(t.getHours() + "", t.getMinutes() + ""));


        }

//        if (data.getBreak_time() != null && data.getBreak_time().equals("00:00")) {
//            mHolder.break_layout.setVisibility(View.GONE);
//            mHolder.divider_two.setVisibility(View.GONE);
//        } else {
//            mHolder.break_layout.setVisibility(View.VISIBLE);
//            mHolder.divider_two.setVisibility(View.VISIBLE);
//        }

        mHolder.header.setText(data.getSchedule_no());
        mHolder.tvIntime.setText(data.getIn_time());
        mHolder.tvOuttime.setText(data.getOut_time());
        if (data.getSchedule_no().equals(GlobalUtils.SESSION_ENDED)) {
            mHolder.main_btn.setVisibility(View.GONE);

        } else {
            mHolder.header.setText(data.getSchedule_no());

        }

        initListener(position);
        setListenerForView();

        return convertView;
    }

    //after click the main btn check in/check out
    private void afterClickClockIn(final int position) {

        if (mListData.get(position).getSchedule_no().equals(GlobalUtils.CLOCK_IN)) {

            if (GlobalUtils.no_counting) {

                if (position == 0) {

                    //Dialog call
                    GlobalUtils.showDialogToGetUserInput(mContext, new DialogForValueCallback() {
                        @Override
                        public void onAction1(String productivity, String hour, String minutes) {
                            // Toast.makeText(mContext, productivity + " " + hour + " " + minutes, Toast.LENGTH_LONG).show();
                            GlobalUtils.no_counting = false;
                            //target productivity and total treatment time is fixed,thats why saved in the sharedPreference
                            SharedPreferences.Editor editor = GlobalUtils.preferences(mContext).edit();
                            editor.putString(Constants.TARGET_PRODUCTIVITY, productivity);
                            editor.putString(Constants.TOTAL_TREATMENT_TIME, GlobalUtils.convertTimeInMinutes(hour, minutes));
                            editor.commit();

                            GlobalUtils.TARGET_PRODUCTIVITY = GlobalUtils.preferences(mContext).getString(Constants.TARGET_PRODUCTIVITY, "0");
                            GlobalUtils.TOTAL_TREATMENT_TIME = GlobalUtils.preferences(mContext).getString(Constants.TOTAL_TREATMENT_TIME, "0");


                            Calendar now = Calendar.getInstance();
                            TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                    schedule = mListData.get(position);
                                    schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
                                    String ampm = "";
                                    if (hourOfDay >= 12) {
                                        if (hourOfDay != 12)
                                            hourOfDay -= 12;
                                        ampm = "PM";
                                    } else {
                                        if (hourOfDay == 0)
                                            hourOfDay += 12;
                                        ampm = "AM";
                                    }

                                    schedule.setIn_time(String.format("%02d", hourOfDay)
                                            + ":"
                                            + String.format("%02d", minute)
                                            + " "
                                            + ampm);

                                    schedule.setOut_time("00:00");
                                    schedule.setBreak_time("00:00");
                                    schedule.setWorked_in_that_session("00:00");
                                    schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);
                                    schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
                                    schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                                    schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));
                                    //save the target treatment_time which is fixed
                                    SharedPreferences.Editor editor = GlobalUtils.preferences(mContext).edit();
                                    editor.putString(Constants.TARGET_TREATMENT_TIME, schedule.getTarget_treatment_time());
                                    editor.commit();
                                    //calculate target clockout time depending on the target treatment time(intime + target treatment time)
                                    schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));

                                    ((MainActivity) mContext).addItemToMainScheduleList(schedule);

                                }
                            }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);

                            tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

                        }

                        @Override
                        public void onAction3(int type, String hour, String minutes) {

                        }

                        @Override
                        public void onAction2() {

                        }
                    });


                } else {

                    Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

                            schedule = mListData.get(position);
                            schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
                            String ampm = "";
                            if (hourOfDay >= 12) {
                                if (hourOfDay != 12)
                                    hourOfDay -= 12;
                                ampm = "PM";
                            } else {
                                if (hourOfDay == 0)
                                    hourOfDay += 12;
                                ampm = "AM";
                            }

                            String intime = String.format("%02d", hourOfDay)
                                    + ":"
                                    + String.format("%02d", minute)
                                    + " "
                                    + ampm;
                            Schedule pre_sch = mListData.get(position - 1);

                            if (!GlobalUtils.check_conflict(GlobalUtils.get_time(intime), GlobalUtils.get_time(pre_sch.getOut_time()))) {
                                Log.e("Conflicted", "Conflicted");
                                GlobalUtils.showInfoDialog(mContext, "Error", "Your entered time conflicted other times" +
                                        ",Please enter a non conflicted value", "OK", null);
                                GlobalUtils.no_counting = true;
                            }else {
                                GlobalUtils.no_counting = false;

                                schedule.setIn_time(intime);
                                schedule.setOut_time("00:00");
                                schedule.setBreak_time("00:00");
                                schedule.setWorked_in_that_session("00:00");
                                schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);
                                schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
                                schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                                schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));

                                schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));

                                String out = pre_sch.getOut_time();
                                Time break_time = GlobalUtils.get_break(GlobalUtils.get_time(schedule.getIn_time()), GlobalUtils.get_time(out));
                                pre_sch.setBreak_time(String.format("%02d", break_time.getHours()) + ":" + String.format("%02d", break_time.getMinutes()));

                                schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(pre_sch.getTarget_clockout_time()), GlobalUtils.convertTimeInMinutes(GlobalUtils.get_time(pre_sch.getBreak_time() + " NO").getHours() + "", GlobalUtils.get_time(pre_sch.getBreak_time() + " NO").getMinutes() + "")));


                                ((MainActivity) mContext).update_db(pre_sch);
                                ((MainActivity) mContext).addItemToMainScheduleList(schedule);
                            }

                        }
                    }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);

                    tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

                }
            } else {
                //currently running an session
                GlobalUtils.showInfoDialog(mContext, "Error", "A session is ongoing currently,please clock out the on going" +
                        "ongoing session", "OK", new SCDialogCallback() {
                    @Override
                    public void onAction1() {

                    }

                    @Override
                    public void onAction2() {

                    }

                    @Override
                    public void onAction3() {

                    }

                    @Override
                    public void onAction4() {

                    }
                });

            }
        } else if (mListData.get(position).getSchedule_no().equals(GlobalUtils.CLOCK_OUT)) {


            Calendar now = Calendar.getInstance();
            TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

                    String ampm = "";
                    if (hourOfDay >= 12) {
                        if (hourOfDay != 12)
                            hourOfDay -= 12;
                        ampm = "PM";
                    } else {
                        if (hourOfDay == 0)
                            hourOfDay += 12;
                        ampm = "AM";
                    }
                    schedule = mListData.get(position);

                    String outtime = String.format("%02d", hourOfDay)
                            + ":"
                            + String.format("%02d", minute)
                            + " "
                            + ampm;

                    if (!GlobalUtils.check_conflict(GlobalUtils.get_time(outtime), GlobalUtils.get_time(schedule.getIn_time()))) {
                        Log.e("Conflicted", "Conflicted");
                        GlobalUtils.showInfoDialog(mContext, "Error", "Your entered time conflicted other times" +
                                ",Please enter a non conflicted value", "OK", null);
                    }else {

                        schedule.setOut_time(outtime);

                        Time working_hours = GlobalUtils.get_worked_in_that_session(GlobalUtils.get_time(schedule.getIn_time()), GlobalUtils.get_time(schedule.getOut_time()));

                        schedule.setWorked_in_that_session(String.format("%02d", working_hours.getHours())
                                + ":"
                                + String.format("%02d", working_hours.getMinutes())
                                + " "
                                + "N/A");

                        Time actual_treatment_time = GlobalUtils.get_actual_working_hours(GlobalUtils.get_time(schedule.getIn_time()), GlobalUtils.get_time(schedule.getOut_time()));
                        schedule.setActual_treatment_time(String.format("%02d", actual_treatment_time.getHours())
                                + ":"
                                + String.format("%02d", actual_treatment_time.getMinutes()));
                        schedule.setSchedule_no(GlobalUtils.SESSION_ENDED);
                        schedule.setActual_productivity(GlobalUtils.get_productivity(GlobalUtils.TOTAL_TREATMENT_TIME, GlobalUtils.convertTimeInMinutes(actual_treatment_time.getHours() + "", actual_treatment_time.getMinutes() + "")));
                        schedule.setActual_clockout_time(schedule.getOut_time());

                        ((MainActivity) mContext).updateItemToMainScheduleList(schedule);
                        GlobalUtils.no_counting = true;
                    }


                }
            }, now.get(Calendar.HOUR), now.get(Calendar.MINUTE), false);

            tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

        }


    }


//  productivity = (total_treatment_time/actual_treatment_time)*100

    private void initListener(final int position) {
        mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.add:
                        afterClickClockIn(position);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    private void setListenerForView() {
        mHolder.main_btn.setOnClickListener(mOnClickListener);
    }
}
