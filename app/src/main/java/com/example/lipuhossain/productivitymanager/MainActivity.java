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
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.adapters.BookNowDateListAdapter;
import com.example.lipuhossain.productivitymanager.adapters.ScheduleAdapter;
import com.example.lipuhossain.productivitymanager.database.DatabaseHelper;
import com.example.lipuhossain.productivitymanager.models.CustomDate;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.models.Time;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Calendar;

import io.netopen.hotbitmapgg.library.view.RingProgressBar;

public class MainActivity extends AppCompatActivity {

    private BookNowDateListAdapter adapter = null;
    private ViewPager pager = null;
    private ArrayList<CustomDate> mListDate = null;
    private ArrayList<Schedule> mListScheduleData = null;
    private RingProgressBar progress_target_productivity = null;
    private RingProgressBar progress_actual_productivity = null;
    private ListView mListSchedule = null;
    private ScheduleAdapter scheduleAdapter = null;
    private View stickyViewSpacer = null;
    private RelativeLayout status_bar = null;


    private TextView tv_target_treatment = null;
    private TextView tv_target_clock_out = null;
    private TextView tv_actual_treatment_time = null;
    private TextView tv_actual_out_time = null;


    // Database Helper
    private DatabaseHelper db = null;

    @Override
    protected void onRestart() {
        super.onRestart();
        update_list_from_db();
        if (mListScheduleData.size() != 0) {
            GlobalUtils.TARGET_PRODUCTIVITY = mListScheduleData.get(0).getTarget_productivity();
            generate_calculate_show_view();


        }
        initScheduleList();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(getApplicationContext());

        findViewByIds();
        initScheduleList();
        initViewPager();

    }

    private void initScheduleList() {
        if (db.CheckIsDataAlreadyInDBorNot(GlobalUtils.getCurrentDate().getFormattedDate())) {
            mListScheduleData = db.getAllSchedulesByDate(GlobalUtils.getCurrentDate().getFormattedDate());
            Schedule schedule = new Schedule();
            schedule.setSchedule_no(GlobalUtils.CLOCK_IN);
            mListScheduleData.add(schedule);
        } else {
            mListScheduleData = new ArrayList<Schedule>();
            Schedule schedule = new Schedule();
            schedule.setSchedule_no(GlobalUtils.CLOCK_IN);
            mListScheduleData.add(schedule);
        }

        scheduleAdapter = new ScheduleAdapter(this,
                mListScheduleData, db);
        mListSchedule.setAdapter(scheduleAdapter);
    }

    private void findViewByIds() {

        tv_target_treatment = (TextView) findViewById(R.id.tv_target_treatment);
        tv_target_clock_out = (TextView) findViewById(R.id.tv_target_clock_out);
        tv_actual_treatment_time = (TextView) findViewById(R.id.tv_actual_treatment_time);
        tv_actual_out_time = (TextView) findViewById(R.id.tv_actual_out_time);

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

        if (GlobalUtils.getCurrentDate().getFormattedDate().equals(mListDate.get(index).getFormattedDate())) {
            mListSchedule.setVisibility(View.VISIBLE);
           // generate_calculate_show_view();
        } else {
            mListSchedule.setVisibility(View.INVISIBLE);
            reset_Views();
        }
        System.out.println("" + mListDate.get(index).getDate() + " " + mListDate.get(index).getMonth() + " " + mListDate.get(index).getYear());
        adapter.setCurrentItem(index);
        adapter.notifyDataSetChanged();
    }


    public void addItemToMainScheduleList(Schedule schedule) {
        Schedule nxt_schedule = new Schedule();
        nxt_schedule.setSchedule_no(GlobalUtils.CLOCK_IN);
        add_to_db(schedule);
        update_list_from_db();
        mListScheduleData.add(nxt_schedule);
        //i dont know why bt scheduleadapter.notifydatasetchanged() is not working,so reinitializing it
        scheduleAdapter = new ScheduleAdapter(this, mListScheduleData, db);
        mListSchedule.setAdapter(scheduleAdapter);
    }

    private void update_list_from_db() {
        mListScheduleData = db.getAllSchedulesByDate(GlobalUtils.getCurrentDate().getFormattedDate());

    }

    public void updateItemToMainScheduleList(Schedule schedule) {

        for (Schedule a_schedule : mListScheduleData) {
            if (a_schedule.getId() != null && a_schedule.getId().equals(schedule.getId())) {
                a_schedule.setDate(schedule.getDate());
                a_schedule.setId(schedule.getId());
                a_schedule.setIn_time(schedule.getIn_time());
                a_schedule.setOut_time(schedule.getOut_time());
                a_schedule.setBreak_time(schedule.getBreak_time());
                a_schedule.setSchedule_no(schedule.getSchedule_no());
                a_schedule.setTotal_treatment_time(schedule.getTotal_treatment_time());
                a_schedule.setTarget_treatment_time(schedule.getTarget_treatment_time());
                a_schedule.setActual_treatment_time(schedule.getActual_treatment_time());
                a_schedule.setTarget_productivity(schedule.getTarget_productivity());
                a_schedule.setActual_productivity(schedule.getActual_productivity());
                a_schedule.setTarget_clockout_time(schedule.getTarget_clockout_time());
                a_schedule.setActual_clockout_time(schedule.getActual_clockout_time());

                update_db(a_schedule);
            }
        }
        update_list_from_db();
        scheduleAdapter.notifyDataSetChanged();
        generate_calculate_show_view();
    }

    public void generate_calculate_show_view() {
        int items = mListScheduleData.size();

        GlobalUtils.calculated_schedule.setTarget_productivity(mListScheduleData.get(0).getTarget_productivity());
        if (mListScheduleData.get(mListScheduleData.size() - 1).getTarget_clockout_time() != null) {
            GlobalUtils.calculated_schedule.setTarget_clockout_time(mListScheduleData.get(mListScheduleData.size() - 1).getTarget_clockout_time());
            GlobalUtils.calculated_schedule.setActual_clockout_time(mListScheduleData.get(mListScheduleData.size() - 1).getActual_clockout_time());

        }else{
            GlobalUtils.calculated_schedule.setTarget_clockout_time(mListScheduleData.get(mListScheduleData.size() - 2).getTarget_clockout_time());
            GlobalUtils.calculated_schedule.setActual_clockout_time(mListScheduleData.get(mListScheduleData.size() - 2).getActual_clockout_time());

        }

        int target_treatment_time = Integer.parseInt(mListScheduleData.get(0).getTarget_treatment_time());
        int updated_target_hour = target_treatment_time / 60;
        int updated_target_minute = target_treatment_time % 60;

        GlobalUtils.calculated_schedule.setTarget_treatment_time(updated_target_hour + ":" + updated_target_minute);

        int minute = 0;

        for (Schedule schedule : mListScheduleData
                ) {
            if(schedule.getActual_treatment_time()!= null) {
                Time t = GlobalUtils.get_time(schedule.getActual_treatment_time() + " NO");
                minute += Integer.parseInt(GlobalUtils.convertTimeInMinutes(t.getHours() + "", t.getMinutes() + ""));
            }
        }
        int updated_hour = minute / 60;
        int updated_minute = minute % 60;


        GlobalUtils.calculated_schedule.setActual_treatment_time(updated_hour + ":" + updated_minute);
       GlobalUtils.calculated_schedule.setActual_productivity(GlobalUtils.get_productivity(mListScheduleData.get(0).getTotal_treatment_time(), minute + ""));

        update_Views(GlobalUtils.calculated_schedule);


    }

    public void update_a_schedule(Schedule schedule) {

        for (Schedule a_schedule : mListScheduleData) {
            if (a_schedule.getId() != null && a_schedule.getId().equals(schedule.getId())) {
                a_schedule.setDate(schedule.getDate());
                a_schedule.setId(schedule.getId());
                a_schedule.setIn_time(schedule.getIn_time());
                a_schedule.setOut_time(schedule.getOut_time());
                a_schedule.setBreak_time(schedule.getBreak_time());
                a_schedule.setSchedule_no(schedule.getSchedule_no());
                a_schedule.setTotal_treatment_time(schedule.getTotal_treatment_time());
                a_schedule.setTarget_treatment_time(schedule.getTarget_treatment_time());
                a_schedule.setActual_treatment_time(schedule.getActual_treatment_time());
                a_schedule.setTarget_productivity(schedule.getTarget_productivity());
                a_schedule.setActual_productivity(schedule.getActual_productivity());
                a_schedule.setTarget_clockout_time(schedule.getTarget_clockout_time());
                a_schedule.setActual_clockout_time(schedule.getActual_clockout_time());
            }
        }
    }

    private void add_to_db(Schedule schedule) {
        db.createSchedule(schedule);
    }

    public void update_db(Schedule schedule) {
        db.updateSchedule(schedule);
    }


    private void update_Views(Schedule schedule) {

        tv_target_treatment.setText(schedule.getTarget_treatment_time());
        tv_target_clock_out.setText(schedule.getTarget_clockout_time());
        tv_actual_treatment_time.setText(schedule.getActual_treatment_time());
        tv_actual_out_time.setText(schedule.getActual_clockout_time());

        if (schedule.getTarget_productivity() != null && !schedule.getTarget_productivity().equals(""))
            GlobalUtils.showProgress(progress_target_productivity, Integer.parseInt(schedule.getTarget_productivity()));
        else
            GlobalUtils.showProgress(progress_target_productivity, 0);


        if (schedule.getActual_productivity() != null && !schedule.getActual_productivity().equals(""))
            GlobalUtils.showProgress(progress_actual_productivity, Integer.parseInt(schedule.getActual_productivity()));
        else
            GlobalUtils.showProgress(progress_actual_productivity, 0);


    }


    private void reset_Views() {

        tv_target_treatment.setText("00:00");
        tv_target_clock_out.setText("00:00");
        tv_actual_treatment_time.setText("00:00");
        tv_actual_out_time.setText("00:00");

        GlobalUtils.resetProgress(progress_target_productivity);
        GlobalUtils.resetProgress(progress_actual_productivity);

    }


}
