package ben.work.lipuhossain.productivitymanager.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import ben.work.lipuhossain.productivitymanager.DetailsActivity;
import ben.work.lipuhossain.productivitymanager.adapters.holder.HistoryHolder;
import ben.work.lipuhossain.productivitymanager.models.History;

import java.util.ArrayList;

/**
 * Created by lipuhossain on 10/9/16.
 */

public class HistoryAdapter extends BaseAdapter {
    private ArrayList<History> mListData = null;
    private Context mContext = null;
    private HistoryHolder mHolder = null;
    private View.OnClickListener mOnClickListener = null;

    public HistoryAdapter(ArrayList<History> mListData, Context mContext) {
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
            convertView = inflator.inflate(ben.work.lipuhossain.productivitymanager.R.layout.history_list_item, null);
            mHolder = new HistoryHolder();
            mHolder.tvDate = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tvdate);
            mHolder.tvTarget = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tvtarget);
            mHolder.tvActual = (TextView) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.tvactual);
            mHolder.main = (LinearLayout) convertView.findViewById(ben.work.lipuhossain.productivitymanager.R.id.main_btn);

            convertView.setTag(mHolder);
        } else {
            mHolder = (HistoryHolder) convertView.getTag();
        }
        History data = mListData.get(position);


        mHolder.tvDate.setText(data.getDate());
        mHolder.tvTarget.setText(data.getTarget_productivity()+"%");
        mHolder.tvActual.setText(data.getActual_productivity()+"%");

        initListener(position);
        setListenerForView();

        return convertView;
    }

    private void initListener(final int position) {
        mOnClickListener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case ben.work.lipuhossain.productivitymanager.R.id.main_btn:
                        afterClickClockIn(position);
                        break;
                    default:
                        break;
                }

            }
        };
    }

    private void afterClickClockIn(int position) {
        Intent intent = new Intent(mContext, DetailsActivity.class);
        intent.putExtra("date",mListData.get(position).getDate());
        mContext.startActivity(intent);
    }

    private void setListenerForView() {
        mHolder.main.setOnClickListener(mOnClickListener);
    }
}
