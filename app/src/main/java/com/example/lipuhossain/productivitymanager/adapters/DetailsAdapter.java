package com.example.lipuhossain.productivitymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.DetailsActivity;
import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.adapters.holder.HistoryHolder;
import com.example.lipuhossain.productivitymanager.models.History;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.models.Time;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

/**
 * Created by lipuhossain on 10/9/16.
 */

public class DetailsAdapter extends BaseAdapter {
    private ArrayList<Schedule> mListData = null;
    private Context mContext = null;
    private HistoryHolder mHolder = null;
    private View.OnClickListener mOnClickListener = null;

    public DetailsAdapter(ArrayList<Schedule> mListData, Context mContext) {
        this.mListData = mListData;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflator.inflate(R.layout.list_item_history, null);
            mHolder = new HistoryHolder();
            mHolder.tvDate = (TextView) convertView.findViewById(R.id.tvintime);
            mHolder.tvTarget = (TextView) convertView.findViewById(R.id.tvouttime);
            mHolder.tvActual = (TextView) convertView.findViewById(R.id.tvbreak);
            mHolder.tvWorked = (TextView) convertView.findViewById(R.id.tvWorked);

            convertView.setTag(mHolder);
        } else {
            mHolder = (HistoryHolder) convertView.getTag();
        }
        Schedule data = mListData.get(position);

        Time break_time = GlobalUtils.get_time(data.getBreak_time()+" N/A");
        Time worked_time = GlobalUtils.get_time(data.getWorked_in_that_session()+" N/A");

        mHolder.tvDate.setText(data.getIn_time());
        mHolder.tvTarget.setText(data.getOut_time());
        mHolder.tvActual.setText(String.format("%02d%2$s", break_time.getHours(),"h")
                + ":"
                + String.format("%02d%2$s", break_time.getMinutes(),"m"));
        mHolder.tvWorked.setText(String.format("%02d%2$s", worked_time.getHours(),"h")
                + ":"
                + String.format("%02d%2$s", worked_time.getMinutes(),"m"));


        return convertView;
    }

    }