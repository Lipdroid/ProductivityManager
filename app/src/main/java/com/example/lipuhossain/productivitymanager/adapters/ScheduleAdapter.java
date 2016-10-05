package com.example.lipuhossain.productivitymanager.adapters;

import android.app.Activity;
import android.content.Context;
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


            convertView.setTag(mHolder);
        } else {
            mHolder = (ScheduleHolder) convertView.getTag();
        }
        Schedule data = mListData.get(position);

        if (data.getSchedule_no().equals(GlobalUtils.CLOCK_IN)) {
            mHolder.time_layout.setVisibility(View.GONE);
        } else {
            mHolder.time_layout.setVisibility(View.VISIBLE);
        }
        if(data.getBreak_time() != null && data.getBreak_time().equals("00:00")) {
            mHolder.break_layout.setVisibility(View.GONE);
        }else{
            mHolder.break_layout.setVisibility(View.VISIBLE);
        }

        mHolder.header.setText(data.getSchedule_no());
        mHolder.tvIntime.setText(data.getIn_time());
        mHolder.tvOuttime.setText(data.getOut_time());
        mHolder.tvBreaktime.setText(data.getBreak_time());
        mHolder.header.setText(data.getSchedule_no());

        initListener(position);
        setListenerForView();

        return convertView;
    }

    private void afterClickClockIn(final int position) {

        if (mListData.get(position).getSchedule_no().equals(GlobalUtils.CLOCK_IN)) {

            if (GlobalUtils.no_counting) {


                if (position == 0) {

                    //Dialog call
                    GlobalUtils.showDialogToGetUserInput(mContext, new DialogForValueCallback() {
                        @Override
                        public void onAction1(String productivity, String hour, String minutes) {
                            Toast.makeText(mContext, productivity + " " + hour + " " + minutes, Toast.LENGTH_LONG).show();
                            GlobalUtils.no_counting = false;

                            GlobalUtils.TARGET_PRODUCTIVITY = productivity;
                            GlobalUtils.TOTAL_TREATMENT_TIME = GlobalUtils.convertTimeInMinutes(hour, minutes);

                            Calendar now = Calendar.getInstance();
                            TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                                @Override
                                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                                    schedule = mListData.get(position);
                                    schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
                                    String ampm = "";
                                    if(hourOfDay >= 12){
                                        if (hourOfDay != 12)
                                            hourOfDay -= 12;
                                        ampm = "PM";
                                    }else{
                                        if (hourOfDay == 0)
                                            hourOfDay += 12;
                                        ampm = "AM";
                                    }
                                    schedule.setIn_time(hourOfDay+":"+minute+" "+ampm);
                                    schedule.setOut_time("00:00");
                                    schedule.setBreak_time("00:00");
                                    schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);
                                    schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
                                    schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                                    schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));

                                    schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));

                                    ((MainActivity) mContext).addItemToMainScheduleList(schedule);

                                }
                            },now.get(Calendar.HOUR),now.get(Calendar.MINUTE),false);

                            tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

                        }

                        @Override
                        public void onAction2() {

                        }
                    });

//                GlobalUtils.showConfirmDialog(mContext,
//                        "Confirmation", "Your current Target Productivity is " + GlobalUtils.TARGET_PRODUCTIVITY + ",is it ok?or want to change it?",
//                        "OK",
//                        "CHANGE",
//                        new SCDialogCallback() {
//                            @Override
//                            public void onAction1() {
//
//                                schedule = mListData.get(position);
//                                schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
//                                schedule.setIn_time(GlobalUtils.getCurrentTime());
//                                schedule.setOut_time("00:00");
//                                schedule.setBreak_time("00:00");
//                                schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);
//                                schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
//                                schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
//                                schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));
//                                schedule.setTarget_clockout_time(GlobalUtils.add_hours_to_time(schedule.getIn_time(), schedule.getTarget_treatment_time()));
//
//                                ((MainActivity) mContext).addItemToMainScheduleList(schedule);
//
//                            }
//
//                            @Override
//                            public void onAction2() {
//
//                            }
//
//                            @Override
//                            public void onAction3() {
//
//                            }
//
//                            @Override
//                            public void onAction4() {
//
//                            }
//                        }
//                );
                } else {

                     Calendar now = Calendar.getInstance();
                    TimePickerDialog tpd = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                            GlobalUtils.no_counting = false;

                            schedule = mListData.get(position);
                            schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
                            String ampm = "";
                            if(hourOfDay >= 12){
                                if (hourOfDay != 12)
                                    hourOfDay -= 12;
                                ampm = "PM";
                            }else{
                                if (hourOfDay == 0)
                                    hourOfDay += 12;
                                ampm = "AM";
                            }
                            schedule.setIn_time(hourOfDay+":"+minute+" "+ampm);
                            schedule.setOut_time("00:00");
                            schedule.setBreak_time("00:00");
                            schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);
                            schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
                            schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                            schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));

                            schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));

                            Schedule pre_sch = mListData.get(position-1);
                            String out = pre_sch.getOut_time();
                            Time break_time = GlobalUtils.get_break(GlobalUtils.get_time(schedule.getIn_time()),GlobalUtils.get_time(out));

                            pre_sch.setBreak_time(break_time.getHours()+":"+break_time.getMinutes());

                            schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(pre_sch.getTarget_clockout_time()), GlobalUtils.convertTimeInMinutes(GlobalUtils.get_time(pre_sch.getBreak_time()+" NO").getHours()+"",GlobalUtils.get_time(pre_sch.getBreak_time()+" NO").getMinutes()+"")));


                            ((MainActivity)mContext).update_db(pre_sch);

                            ((MainActivity) mContext).addItemToMainScheduleList(schedule);

                        }
                    },now.get(Calendar.HOUR),now.get(Calendar.MINUTE),false);

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
            schedule = mListData.get(position);
            schedule.setOut_time(GlobalUtils.getCurrentTime());
            Time actual_treatment_time = GlobalUtils.get_actual_working_hours(GlobalUtils.get_time(schedule.getIn_time()),GlobalUtils.get_time(schedule.getOut_time()));
            schedule.setActual_treatment_time(actual_treatment_time.getHours()+":"+actual_treatment_time.getMinutes());
            schedule.setSchedule_no(GlobalUtils.SESSION_ENDED);
            schedule.setActual_productivity(GlobalUtils.get_productivity(schedule.getTotal_treatment_time(),GlobalUtils.convertTimeInMinutes(actual_treatment_time.getHours()+"",actual_treatment_time.getMinutes()+"")));
            schedule.setActual_clockout_time(schedule.getOut_time());

            ((MainActivity) mContext).updateItemToMainScheduleList(schedule);
            GlobalUtils.no_counting = true;



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