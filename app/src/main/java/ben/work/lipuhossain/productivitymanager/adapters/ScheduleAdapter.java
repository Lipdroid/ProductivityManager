package ben.work.lipuhossain.productivitymanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ben.work.lipuhossain.productivitymanager.MainActivity;
import ben.work.lipuhossain.productivitymanager.adapters.holder.ScheduleHolder;
import ben.work.lipuhossain.productivitymanager.constants.Constants;
import ben.work.lipuhossain.productivitymanager.database.DatabaseHelper;
import ben.work.lipuhossain.productivitymanager.interfaces.DialogForValueCallback;
import ben.work.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import ben.work.lipuhossain.productivitymanager.models.Schedule;
import ben.work.lipuhossain.productivitymanager.models.Time;
import ben.work.lipuhossain.productivitymanager.utils.GlobalUtils;
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
            convertView = inflator.inflate(ben.work.lipuhossain.productivitymanager.R.layout.item_schedule, null);

            mHolder = new ScheduleHolder();

            mHolder.time_layout = (LinearLayout) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.time);
            mHolder.main_btn = (LinearLayout) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.add);
            mHolder.tvIntime = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.intime);
            mHolder.tvOuttime = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.outtime);
            mHolder.tvBreaktime = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.breaktime);
            mHolder.header = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.header_text);
            mHolder.break_layout = (RelativeLayout) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.rl_break);

            mHolder.tv_label_patient_time = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tv_label_patient_time);
            mHolder.tv_label_clock_in = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tv_label_clock_in);
            mHolder.tv_label_clock_out = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tv_label_clock_out);


            mHolder.divider_one = (View) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.divider_one);
            mHolder.divider_two = (View) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.divider_two);


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
        mHolder.tv_label_clock_in.setText(convert_position_to_places(position + 1) + " Clock In");
        mHolder.tv_label_clock_out.setText(convert_position_to_places(position + 1) + " Clock Out");

        if (data.getSchedule_no().equals(GlobalUtils.SESSION_ENDED)) {
            mHolder.main_btn.setVisibility(View.GONE);

        } else {
            mHolder.header.setText(data.getSchedule_no());
            mHolder.main_btn.setVisibility(View.VISIBLE);
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
                                    GlobalUtils.no_counting = false;
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
                            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);

                            tpd.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    GlobalUtils.no_counting = true;
                                }
                            });
                            tpd.setTitle("Clock In Time");
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
                                GlobalUtils.showInfoDialog(mContext, "Error", "Time entered conflicts with earlier time" +
                                        ", please resolve", "OK", null);
                                GlobalUtils.no_counting = true;
                            } else {
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

                                schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(pre_sch.getTarget_clockout_time()), GlobalUtils.convertTimeInMinutes(GlobalUtils.get_time(pre_sch.getBreak_time() + " N/A").getHours() + "", GlobalUtils.get_time(pre_sch.getBreak_time() + " N/A").getMinutes() + "")));

                                ((MainActivity) mContext).clock_in_so_invisible_the_item();
                                ((MainActivity) mContext).update_db(pre_sch);
                                ((MainActivity) mContext).addItemToMainScheduleList(schedule);
                            }

                        }
                    }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
                    tpd.setTitle("Clock In Time");
                    tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

                }
            } else {
                //currently running an session
                GlobalUtils.showInfoDialog(mContext, "Error", "Please clock out of previous session " +
                        "before you clock in again", "OK", new SCDialogCallback() {
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
                        GlobalUtils.showInfoDialog(mContext, "Error", "Time entered conflicts with earlier time" +
                                ", please resolve", "OK", null);
                    } else {

                        ((MainActivity) mContext).clock_out_so_visible_the_item();
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
            }, now.get(Calendar.HOUR_OF_DAY), now.get(Calendar.MINUTE), false);
            tpd.setTitle("Clock Out Time");
            tpd.show(((Activity) mContext).getFragmentManager(), "Timepickerdialog");

        }


    }


//  productivity = (total_treatment_time/actual_treatment_time)*100

    private void initListener(final int position) {
        mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case ben.work.lipuhossain.productivitymanager.R.id.add:
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

    private String convert_position_to_places(int position) {
        String result = "";
        switch (position) {
            case 1:
                result = position + "st";
                break;
            case 2:
                result = position + "nd";
                break;
            case 3:
                result = position + "rd";
                break;
            default:
                result = position + "th";
                break;

        }
        return result;
    }
}
