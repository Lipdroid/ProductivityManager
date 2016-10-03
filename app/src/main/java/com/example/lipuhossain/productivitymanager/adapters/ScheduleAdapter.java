package com.example.lipuhossain.productivitymanager.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.MainActivity;
import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.adapters.holder.ScheduleHolder;
import com.example.lipuhossain.productivitymanager.database.DatabaseHelper;
import com.example.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

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
        }else{
            mHolder.time_layout.setVisibility(View.VISIBLE);
        }
        mHolder.break_layout.setVisibility(View.GONE);

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

                GlobalUtils.no_counting = false;

                GlobalUtils.showConfirmDialog(mContext,
                        "Confirmation", "Your current Target Productivity is " + GlobalUtils.TARGET_PRODUCTIVITY + ",is it ok?or want to change it?",
                        "OK",
                        "CHANGE",
                        new SCDialogCallback() {
                            @Override
                            public void onAction1() {

                                schedule = mListData.get(position);
                                schedule.setDate(GlobalUtils.getCurrentDate().getFormattedDate());
                                schedule.setIn_time(GlobalUtils.getCurrentTime());
                                schedule.setOut_time("00:00");
                                schedule.setBreak_time("00:00");
                                schedule.setSchedule_no(GlobalUtils.CLOCK_OUT);

                                ((MainActivity) mContext).addItemToMainScheduleList(schedule);

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
                        }
                );
            } else {
                //currently running an session

            }
        } else if (mListData.get(position).getSchedule_no().equals(GlobalUtils.CLOCK_OUT)) {

            schedule = mListData.get(position);
            schedule.setOut_time(GlobalUtils.getCurrentTime());
            schedule.setSchedule_no(GlobalUtils.SESSION_ENDED);
            ((MainActivity) mContext).updateItemToMainScheduleList(schedule);
            GlobalUtils.no_counting = true;


        }


    }


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
