package ben.work.lipuhossain.productivitymanager;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import ben.work.lipuhossain.productivitymanager.adapters.DetailsAdapter;
import ben.work.lipuhossain.productivitymanager.database.DatabaseHelper;
import ben.work.lipuhossain.productivitymanager.models.Schedule;
import ben.work.lipuhossain.productivitymanager.models.Time;
import ben.work.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    private ListView schedule_list = null;
    private DetailsAdapter mListAdapter = null;
    private ArrayList<Schedule> mListData = null;
    // Database Helper
    private DatabaseHelper db = null;
    String date = "";

    private TextView tv_target_treatment = null;
    private TextView tv_target_clock_out = null;
    private TextView tv_actual_treatment_time = null;
    private TextView tv_actual_out_time = null;
    private TextView tv_actual_productivity = null;
    private TextView tv_target_productivity = null;
    private TextView tv_total_time = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_details);
        db = new DatabaseHelper(getApplicationContext());

        schedule_list = (ListView) findViewById(R.id.schedule_list);

        tv_target_treatment = (TextView) findViewById(R.id.tv_target_treatment);
        tv_target_clock_out = (TextView) findViewById(R.id.tv_target_clock_out);
        tv_actual_treatment_time = (TextView) findViewById(R.id.tv_actual_treatment_time);
        tv_actual_out_time = (TextView) findViewById(R.id.tv_actual_out_time);
        tv_actual_productivity = (TextView) findViewById(R.id.actual_product);
        tv_target_productivity = (TextView) findViewById(R.id.target_product);
        tv_total_time = (TextView) findViewById(R.id.total_time);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            date = extras.getString("date");
            // and get whatever type user account id is
            initScheduleList();
        }

    }

    private void initScheduleList() {
        mListData = db.getAllSchedulesByDate(date);
        mListAdapter = new DetailsAdapter(mListData,this);
        schedule_list.setAdapter(mListAdapter);
        generate_calculate_show_view();
    }


    public void afterClickback(View view) {
        finish();
    }


    public void generate_calculate_show_view() {
        int items = mListData.size();

        Schedule calculated_schedule = new Schedule();
        if (mListData.get(mListData.size() - 1).getTarget_clockout_time() != null) {
            calculated_schedule.setTarget_clockout_time(mListData.get(mListData.size() - 1).getTarget_clockout_time());
            calculated_schedule.setActual_clockout_time(mListData.get(mListData.size() - 1).getActual_clockout_time());
            calculated_schedule.setTarget_treatment_time(mListData.get(mListData.size() - 1).getTarget_treatment_time());
            calculated_schedule.setTotal_treatment_time(mListData.get(mListData.size() - 1).getTotal_treatment_time());
            calculated_schedule.setTarget_productivity(mListData.get(mListData.size() - 1).getTarget_productivity());



        } else {
            calculated_schedule.setTarget_clockout_time(mListData.get(mListData.size() - 2).getTarget_clockout_time());
            calculated_schedule.setActual_clockout_time(mListData.get(mListData.size() - 2).getActual_clockout_time());
            calculated_schedule.setTarget_treatment_time(mListData.get(mListData.size() - 2).getTarget_treatment_time());
            calculated_schedule.setTotal_treatment_time(mListData.get(mListData.size() - 2).getTotal_treatment_time());

            calculated_schedule.setTarget_productivity(mListData.get(mListData.size() - 2).getTarget_productivity());


        }

        if (!calculated_schedule.getTarget_treatment_time().equals("0")) {
            int target_treatment_time = Integer.parseInt(calculated_schedule.getTarget_treatment_time());
            int updated_target_hour = target_treatment_time / 60;
            int updated_target_minute = target_treatment_time % 60;
            calculated_schedule.setTarget_treatment_time(String.format("%02d%2$s", updated_target_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_target_minute,"m"));
        }


        int minute = 0;

        for (Schedule schedule : mListData
                ) {
            if (schedule.getActual_treatment_time() != null) {
                Time t = GlobalUtils.get_time(schedule.getActual_treatment_time() + " NO");
                minute += Integer.parseInt(GlobalUtils.convertTimeInMinutes(t.getHours() + "", t.getMinutes() + ""));
            }
        }

        if (minute != 0) {
            int updated_hour = minute / 60;
            int updated_minute = minute % 60;
            calculated_schedule.setActual_treatment_time(String.format("%02d%2$s", updated_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_minute,"m"));
            calculated_schedule.setActual_productivity(GlobalUtils.get_productivity(calculated_schedule.getTotal_treatment_time(), minute + ""));

        }

        if (!calculated_schedule.getTotal_treatment_time().equals("0")) {
            int total_treatment_time = Integer.parseInt(calculated_schedule.getTotal_treatment_time());
            int updated_target_hour = total_treatment_time / 60;
            int updated_target_minute = total_treatment_time % 60;
            calculated_schedule.setTotal_treatment_time(String.format("%02d%2$s", updated_target_hour,"h")
                    + ":"
                    + String.format("%02d%2$s", updated_target_minute,"m"));
        }
        update_Views(calculated_schedule);


    }

    private void update_Views(Schedule schedule) {

        tv_target_treatment.setText(schedule.getTarget_treatment_time());
        tv_target_clock_out.setText(schedule.getTarget_clockout_time());
        tv_actual_treatment_time.setText(schedule.getActual_treatment_time());
        tv_actual_out_time.setText(schedule.getActual_clockout_time());
        tv_total_time.setText(schedule.getTotal_treatment_time());


        if (schedule.getTarget_productivity() != null && !schedule.getTarget_productivity().equals("0")) {
            tv_target_productivity.setText("" + schedule.getTarget_productivity() + "%");
        } else {
            tv_target_productivity.setText("" + 0 + "%");
        }

        if (schedule.getActual_productivity() != null && !schedule.getActual_productivity().equals("0")) {
            tv_actual_productivity.setText("" + schedule.getActual_productivity() + "%");
        } else {
            tv_actual_productivity.setText("" + 0 + "%");
        }
        
    }
}
