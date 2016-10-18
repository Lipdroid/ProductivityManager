package com.example.lipuhossain.productivitymanager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.lipuhossain.productivitymanager.adapters.BookNowDateListAdapter;
import com.example.lipuhossain.productivitymanager.adapters.ScheduleAdapter;
import com.example.lipuhossain.productivitymanager.constants.Constants;
import com.example.lipuhossain.productivitymanager.database.DatabaseHelper;
import com.example.lipuhossain.productivitymanager.interfaces.DialogForValueCallback;
import com.example.lipuhossain.productivitymanager.interfaces.OptionDialogCallback;
import com.example.lipuhossain.productivitymanager.interfaces.SCDialogCallback;
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
    private LinearLayout status_bar = null;


    private TextView tv_target_treatment = null;
    private TextView tv_target_clock_out = null;
    private TextView tv_actual_treatment_time = null;
    private TextView tv_actual_out_time = null;
    private TextView tv_actual_productivity = null;
    private TextView tv_target_productivity = null;
    private TextView tv_total_treatment = null;

    private Context mContext = null;

    // Database Helper
    private DatabaseHelper db = null;

    private FrameLayout pro_frame = null;
    @Override
    protected void onResume() {
        super.onResume();
        //getting previous data
        GlobalUtils.TARGET_PRODUCTIVITY = GlobalUtils.preferences(this).getString(Constants.TARGET_PRODUCTIVITY, "0");
        GlobalUtils.TOTAL_TREATMENT_TIME = GlobalUtils.preferences(this).getString(Constants.TOTAL_TREATMENT_TIME, "0");
        //collect all the data from sqlite database for current date
        update_list_from_db();
        //check if there is data or not
        if (mListScheduleData.size() != 0) {
            generate_calculate_show_view();
        } else {
            //if not reset all the views
            reset_Views();
        }
        //init the list again with data or no data
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
            add_item_with_clock_in();
        } else {
            mListScheduleData = new ArrayList<Schedule>();
            add_item_with_clock_in();
            reset_Views();
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
        tv_actual_productivity = (TextView) findViewById(R.id.actual_product);
        tv_target_productivity = (TextView) findViewById(R.id.target_product);
        tv_total_treatment =  (TextView) findViewById(R.id.tv_total_treatment);

        pro_frame = (FrameLayout) findViewById(R.id.pro_frame);

        pager = (ViewPager) findViewById(R.id.date_pager);
        progress_target_productivity = (RingProgressBar) findViewById(R.id.estimated_progress);
        progress_actual_productivity = (RingProgressBar) findViewById(R.id.actual_progress);
        mListSchedule = (ListView) findViewById(R.id.schedule_list);
        status_bar = (LinearLayout) findViewById(R.id.rl_status);


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

    private void aftererClickTarget() {
        GlobalUtils.showDialogToChangeTotalTime(this, new DialogForValueCallback() {
            @Override
            public void onAction1(String productivity, String hour, String minutes) {

            }

            @Override
            public void onAction3(int type, String hour, String minutes) {
                if (type == 0) {
                    //add with the total treatment time
                    int add_minute = Integer.parseInt(GlobalUtils.convertTimeInMinutes(hour, minutes));
                    int old_minute = Integer.parseInt(GlobalUtils.TOTAL_TREATMENT_TIME);
                    GlobalUtils.TOTAL_TREATMENT_TIME = (old_minute + add_minute) + "";
                    //update all the data in the sqlite database
                    update_all_the_data_according_to_new_total_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                    //update the listview from the new data from database

                } else if (type == 1) {
                    //substract from the total treatment time
                    //add with the total treatment time
                    int add_minute = Integer.parseInt(GlobalUtils.convertTimeInMinutes(hour, minutes));
                    int old_minute = Integer.parseInt(GlobalUtils.TOTAL_TREATMENT_TIME);
                    GlobalUtils.TOTAL_TREATMENT_TIME = (old_minute - add_minute) + "";
                    //update all the data in the sqlite database
                    update_all_the_data_according_to_new_total_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                    //update the listview from the new data from database

                }

            }

            @Override
            public void onAction2() {

            }
        });
    }

    private void update_all_the_data_according_to_new_total_treatment_time(String totalTreatmentTime) {
        boolean updated = false;
        SharedPreferences.Editor editor = GlobalUtils.preferences(this).edit();
        editor.putString(Constants.TOTAL_TREATMENT_TIME, totalTreatmentTime);
        editor.commit();
        //calculate again
        for (Schedule schedule : mListScheduleData) {
            if (schedule.getId() != null) {
                updated = true;
                GlobalUtils.TARGET_PRODUCTIVITY = GlobalUtils.preferences(this).getString(Constants.TARGET_PRODUCTIVITY, "0");
                GlobalUtils.TOTAL_TREATMENT_TIME = GlobalUtils.preferences(this).getString(Constants.TOTAL_TREATMENT_TIME, "0");
                schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
                schedule.setTarget_treatment_time(GlobalUtils.get_target_treatment_hours(GlobalUtils.TARGET_PRODUCTIVITY, GlobalUtils.TOTAL_TREATMENT_TIME));
                //save the target treatment_time which is fixed
                editor.putString(Constants.TARGET_TREATMENT_TIME, schedule.getTarget_treatment_time());
                editor.commit();
                if (schedule.getBreak_time() != null && !schedule.getBreak_time().equals("00:00")) {
                    Time break_time = GlobalUtils.get_time(schedule.getBreak_time()+" NO");
                    Time t = GlobalUtils.get_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));
                    String get_clock_out = GlobalUtils.get_target_clockout(t, GlobalUtils.convertTimeInMinutes(break_time.getHours() + "", break_time.getMinutes() + ""));
                    schedule.setTarget_clockout_time(get_clock_out);
                } else {
                    //calculate target clockout time depending on the target treatment time(intime + target treatment time)
                    schedule.setTarget_clockout_time(GlobalUtils.get_target_clockout(GlobalUtils.get_time(schedule.getIn_time()), schedule.getTarget_treatment_time()));
                }
                schedule.setActual_productivity(GlobalUtils.get_productivity(schedule.getTotal_treatment_time(), schedule.getActual_treatment_time()));
                update_a_schedule(schedule);


            }

        }
        if (updated) {
            update_list_from_db();
            generate_calculate_show_view();
        }
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
            if (mListScheduleData.get(0).getId() != null)
                generate_calculate_show_view();
        } else {
            mListSchedule.setVisibility(View.INVISIBLE);
            reset_Views();
        }
        System.out.println("" + mListDate.get(index).getDate() + " " + mListDate.get(index).getMonth() + " " + mListDate.get(index).getYear());
        adapter.setCurrentItem(index);
        adapter.notifyDataSetChanged();
    }


    public void addItemToMainScheduleList(Schedule schedule) {
        //add to database
        add_to_db(schedule);
        //get all list again to get the all the ids
        update_list_from_db();
        //add an extra item to the list
        Schedule nxt_schedule = new Schedule();
        nxt_schedule.setSchedule_no(GlobalUtils.CLOCK_IN);
        mListScheduleData.add(nxt_schedule);
        //i dont know why bt scheduleadapter.notifydatasetchanged() is not working,so reinitializing it
        scheduleAdapter = new ScheduleAdapter(this, mListScheduleData, db);
        mListSchedule.setAdapter(scheduleAdapter);
        //update the views
        generate_calculate_show_view();
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

        GlobalUtils.TARGET_PRODUCTIVITY = GlobalUtils.preferences(this).getString(Constants.TARGET_PRODUCTIVITY, "0");
        GlobalUtils.TOTAL_TREATMENT_TIME = GlobalUtils.preferences(this).getString(Constants.TOTAL_TREATMENT_TIME, "0");
        GlobalUtils.TARGET_TREATMENT_TIME = GlobalUtils.preferences(this).getString(Constants.TARGET_TREATMENT_TIME, "0");

        GlobalUtils.calculated_schedule.setTarget_productivity(GlobalUtils.TARGET_PRODUCTIVITY);
        GlobalUtils.calculated_schedule.setTotal_treatment_time(GlobalUtils.TOTAL_TREATMENT_TIME);
        GlobalUtils.calculated_schedule.setTarget_treatment_time(GlobalUtils.TARGET_TREATMENT_TIME);


        if (mListScheduleData.get(mListScheduleData.size() - 1).getTarget_clockout_time() != null) {
            GlobalUtils.calculated_schedule.setTarget_clockout_time(mListScheduleData.get(mListScheduleData.size() - 1).getTarget_clockout_time());
            GlobalUtils.calculated_schedule.setActual_clockout_time(mListScheduleData.get(mListScheduleData.size() - 1).getActual_clockout_time());

        } else {
            GlobalUtils.calculated_schedule.setTarget_clockout_time(mListScheduleData.get(mListScheduleData.size() - 2).getTarget_clockout_time());
            GlobalUtils.calculated_schedule.setActual_clockout_time(mListScheduleData.get(mListScheduleData.size() - 2).getActual_clockout_time());

        }

        if (!GlobalUtils.TARGET_TREATMENT_TIME.equals("0")) {
            int target_treatment_time = Integer.parseInt(GlobalUtils.TARGET_TREATMENT_TIME);
            int updated_target_hour = target_treatment_time / 60;
            int updated_target_minute = target_treatment_time % 60;
            GlobalUtils.calculated_schedule.setTarget_treatment_time(String.format("%02d%2$s", updated_target_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_target_minute,"m"));


            int total_treatment_time = Integer.parseInt(GlobalUtils.TOTAL_TREATMENT_TIME);
            updated_target_hour = total_treatment_time / 60;
            updated_target_minute = total_treatment_time % 60;
            GlobalUtils.calculated_schedule.setTotal_treatment_time(String.format("%02d%2$s", updated_target_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_target_minute,"m"));
        }


        int minute = 0;

        for (Schedule schedule : mListScheduleData
                ) {
            if (schedule.getActual_treatment_time() != null) {
                Time t = GlobalUtils.get_time(schedule.getActual_treatment_time() + " NO");
                minute += Integer.parseInt(GlobalUtils.convertTimeInMinutes(t.getHours() + "", t.getMinutes() + ""));
            }
        }

        if (minute != 0) {
            int updated_hour = minute / 60;
            int updated_minute = minute % 60;
            GlobalUtils.calculated_schedule.setActual_treatment_time(String.format("%02d%2$s", updated_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_minute,"m"));
            GlobalUtils.calculated_schedule.setActual_productivity(GlobalUtils.get_productivity(GlobalUtils.TOTAL_TREATMENT_TIME, minute + ""));

        }
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

                update_db(a_schedule);
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
        tv_total_treatment.setText(schedule.getTotal_treatment_time());

        if (schedule.getTarget_productivity() != null && !schedule.getTarget_productivity().equals("0")) {
            tv_target_productivity.setText("(" + schedule.getTarget_productivity() + "%)");
            GlobalUtils.showProgress(progress_target_productivity, Integer.parseInt(schedule.getTarget_productivity()));
        } else {
            tv_target_productivity.setText("(" + 0 + "%)");
            GlobalUtils.showProgress(progress_target_productivity, 0);
        }

        if (schedule.getActual_productivity() != null && !schedule.getActual_productivity().equals("0")) {
            tv_actual_productivity.setText("(" + schedule.getActual_productivity() + "%)");
            GlobalUtils.showProgress(progress_actual_productivity, Integer.parseInt(schedule.getActual_productivity()));
        } else {
            tv_actual_productivity.setText("(" + 0 + "%)");
            GlobalUtils.showProgress(progress_actual_productivity, 0);
        }


    }


    private void reset_Views() {

        tv_target_treatment.setText("00:00");
        tv_target_clock_out.setText("00:00");
        tv_actual_treatment_time.setText("00:00");
        tv_actual_out_time.setText("00:00");

        GlobalUtils.resetProgress(progress_target_productivity);
        GlobalUtils.resetProgress(progress_actual_productivity);
        tv_target_productivity.setText("(" + 0 + "%)");
        tv_actual_productivity.setText("(" + 0 + "%)");


    }


    private void add_item_with_clock_in() {
        Schedule schedule = new Schedule();
        schedule.setSchedule_no(GlobalUtils.CLOCK_IN);
        mListScheduleData.add(schedule);
    }


    public void afterClickedEdit(View view) {
        //i have to implement a popup for options (History,clear,addtime,save)
        GlobalUtils.showOpdtionDialog(this, new OptionDialogCallback() {
            @Override
            public void onActionHistory() {
                Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
                startActivity(intent);
            }

            @Override
            public void onActionChangeTime() {
                if (GlobalUtils.TARGET_TREATMENT_TIME.equals("0")) {
                    GlobalUtils.showInfoDialog(mContext, "Error", "You have not entered your total treatment time yet", "OK", new SCDialogCallback() {
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
                } else {
                    aftererClickTarget();
                }
            }

            @Override
            public void onActionClear() {

            }

            @Override
            public void onActionSave() {

            }

            @Override
            public void onActionCancel() {

            }
        });


    }

    public void afterClickHistory(View view) {
//        Intent intent = new Intent(MainActivity.this,HistoryActivity.class);
//        startActivity(intent);
    }
}
