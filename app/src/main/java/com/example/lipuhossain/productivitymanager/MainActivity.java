package com.example.lipuhossain.productivitymanager;

import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.example.lipuhossain.productivitymanager.adapters.BookNowDateListAdapter;
import com.example.lipuhossain.productivitymanager.adapters.ScheduleAdapter;
import com.example.lipuhossain.productivitymanager.models.CustomDate;
import com.example.lipuhossain.productivitymanager.models.Work;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {

    private BookNowDateListAdapter adapter = null;
    private ViewPager pager = null;
    private ArrayList<CustomDate> mListDate = null;
    private ArrayList<Work> mListScheduleData = null;
    private RingProgressBar progress_target_productivity = null;
    private RingProgressBar progress_actual_productivity = null;
    private ListView mListSchedule = null;
    private ScheduleAdapter scheduleAdapter = null;
    private View stickyViewSpacer = null;
    private RelativeLayout status_bar = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);
        findViewByIds();
        initViewPager();
        initScheduleList();
        // Set the progress bar's progress
        GlobalUtils.showProgress(progress_target_productivity,86);
        GlobalUtils.showProgress(progress_actual_productivity,50);
    }

    private void initScheduleList() {

        mListScheduleData = new ArrayList<Work>();
        Work a = new Work();
        Work b = new Work();
        mListScheduleData.add(a);
        mListScheduleData.add(b);
        scheduleAdapter = new ScheduleAdapter(this,
                mListScheduleData);
        mListSchedule.setAdapter(scheduleAdapter);
    }

    private void findViewByIds() {
        pager = (ViewPager) findViewById(R.id.date_pager);
        progress_target_productivity = (RingProgressBar) findViewById(R.id.estimated_progress);
        progress_actual_productivity = (RingProgressBar) findViewById(R.id.actual_progress);
        mListSchedule = (ListView) findViewById(R.id.schedule_list);
        status_bar = (RelativeLayout) findViewById(R.id.rl_status);


        /* Inflate list header layout */
        LayoutInflater inflater = (LayoutInflater) getSystemService(this.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater.inflate(R.layout.design_parralax, null);
        stickyViewSpacer = listHeader.findViewById(R.id.stickyViewPlaceholder);

        /* Add list view header */
        mListSchedule.addHeaderView(listHeader);

        /* Handle list View scroll events */
        mListSchedule.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                /* Check if the first item is already reached to top.*/
                if (mListSchedule.getFirstVisiblePosition() == 0) {
                    View firstChild = mListSchedule.getChildAt(0);
                    int topY = 0;
                    if (firstChild != null) {
                        topY = firstChild.getTop();
                    }
                    int heroTopY = stickyViewSpacer.getTop();
                    pager.setY(Math.max(0, heroTopY + topY));
                    /* Set the image to scroll half of the amount that of ListView */
                    status_bar.setY(topY * 0.5f);
                }
            }
        });

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
