package com.example.lipuhossain.productivitymanager.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.R;
import com.example.lipuhossain.productivitymanager.models.Work;

import java.util.ArrayList;

/**
 * Created by lipuhossain on 10/1/16.
 */

public class ScheduleAdapter extends BaseAdapter {
    private ArrayList<Work> mListData = null;
    private Context mContext = null;

    @Override
    public int getCount() {
        return mListData.size() + 1;
    }

    public ScheduleAdapter(Context mContext, ArrayList<Work> listData) {
        // TODO Auto-generated constructor stub
        this.mContext = mContext;
        this.mListData = listData;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if(convertView == null) {
        LayoutInflater inflator = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflator.inflate(R.layout.item_schedule, null);
        final LinearLayout layout = (LinearLayout) convertView.findViewById(R.id.time);
        LinearLayout btn = (LinearLayout) convertView.findViewById(R.id.add);

        if(position == 0)
            layout.setVisibility(View.VISIBLE);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.setVisibility(View.VISIBLE);
            }
        });

//        }
        return convertView;
    }
}
