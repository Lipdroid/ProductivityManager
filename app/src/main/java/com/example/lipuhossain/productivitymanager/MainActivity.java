package com.example.lipuhossain.productivitymanager;

import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.lipuhossain.productivitymanager.adapters.BookNowDateListAdapter;
import com.example.lipuhossain.productivitymanager.models.CustomDate;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {

    private BookNowDateListAdapter adapter = null;
    private ViewPager pager = null;
    ArrayList<CustomDate> mListDate = null;
    RingProgressBar progress_estimated_productivity = null;
    RingProgressBar progress_actual_productivity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        findViewByIds();
        initViewPager();
        // Set the progress bar's progress
        progress_estimated_productivity.setProgress(88);
        progress_actual_productivity.setProgress(26);
    }

    private void findViewByIds() {
        pager = (ViewPager) findViewById(R.id.date_pager);
        progress_estimated_productivity = (RingProgressBar) findViewById(R.id.estimated_progress);
        progress_actual_productivity = (RingProgressBar) findViewById(R.id.actual_progress);

    }

    private void initViewPager() {
        mListDate = GlobalUtils.getDates();
        adapter = new BookNowDateListAdapter(this, R.layout.layout_date_item,
                mListDate);
        adapter.setCurrentItem(0);
        pager.setAdapter(adapter);

        // set intial position.
        onPagerItemClick(pager.getChildAt(0), 0);
    }

    public void onPagerItemClick(View view, int index) {
        System.out.println("" + mListDate.get(index).getDate() +" "+mListDate.get(index).getMonth()+" "+mListDate.get(index).getYear());
        adapter.setCurrentItem(index);
        adapter.notifyDataSetChanged();
    }

}
