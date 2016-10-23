package com.example.lipuhossain.productivitymanager;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.lipuhossain.productivitymanager.adapters.HistoryAdapter;
import com.example.lipuhossain.productivitymanager.database.DatabaseHelper;
import com.example.lipuhossain.productivitymanager.interfaces.SeachDialogCallback;
import com.example.lipuhossain.productivitymanager.models.History;
import com.example.lipuhossain.productivitymanager.models.Schedule;
import com.example.lipuhossain.productivitymanager.models.Time;
import com.example.lipuhossain.productivitymanager.utils.GlobalUtils;

import java.util.ArrayList;

public class HistoryActivity extends AppCompatActivity {
    private ListView history_list = null;
    private HistoryAdapter mListAdapter = null;
    private ArrayList<Schedule> mListData = null;
    private ArrayList<History> mListHistoryData = new ArrayList<History>();
    ArrayList<String> dates = new ArrayList<String>();
    private ArrayList<History> searchedlist = new ArrayList<History>();


    // Database Helper
    private DatabaseHelper db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        setContentView(R.layout.activity_history);

        db = new DatabaseHelper(getApplicationContext());

        history_list = (ListView) findViewById(R.id.history_list);
        initHistoryList();
    }

    private void initHistoryList() {
        mListData = db.getAllSchedules();
        mListHistoryData.clear();
        if(!(mListData.size() == 0))
            getHistoryData(mListData);
        mListAdapter = new HistoryAdapter(mListHistoryData,this);
        history_list.setAdapter(mListAdapter);
        //add all history dates
        for (History history:mListHistoryData
                ) {
            dates.add(history.getDate());

        }

    }

    private void getHistoryData(ArrayList<Schedule> mListData) {
        ArrayList<Schedule> calculatelist = new ArrayList<Schedule>();
        String date = mListData.get(0).getDate();
        boolean one_history = false;
        for (Schedule schedule: mListData
             ) {
            if(schedule.getDate().equals(date)){
                calculatelist.add(schedule);
                one_history = true;
            }else{
                one_history = false;
                calculateAndSave(calculatelist);
                calculatelist.clear();
                calculatelist.add(schedule);
                date = schedule.getDate();
            }


        }
        if(one_history)
            calculateAndSave(calculatelist);
    }

    private void calculateAndSave(ArrayList<Schedule> calculatelist) {
        History history = new History();
        history.setDate(calculatelist.get(0).getDate());
        history.setTarget_productivity(calculatelist.get(0).getTarget_productivity());
        int minute = 0;

        for (Schedule schedule : calculatelist
                ) {
            if (schedule.getActual_treatment_time() != null) {
                Time t = GlobalUtils.get_time(schedule.getActual_treatment_time() + " NO");
                minute += Integer.parseInt(GlobalUtils.convertTimeInMinutes(t.getHours() + "", t.getMinutes() + ""));
            }
        }

        if (minute != 0) {
            int updated_hour = minute / 60;
            int updated_minute = minute % 60;
            history.setActual_productivity(GlobalUtils.get_productivity(calculatelist.get(0).getTotal_treatment_time(), minute + ""));

        }


        mListHistoryData.add(history);

    }


    public void afterClickSearch(View view) {

        GlobalUtils.showDialogSearch(this,dates, new SeachDialogCallback() {
            @Override
            public void onAction1(String searched_txt) {
                for (History history:mListHistoryData
                        ) {
                    if(history.getDate().equals(searched_txt)){
                        searchedlist.clear();
                        searchedlist.add(history);
                        mListAdapter = new HistoryAdapter(searchedlist,HistoryActivity.this);
                        history_list.setAdapter(mListAdapter);
                    }

                }

            }

            @Override
            public void onAction2() {

            }

            @Override
            public void onAction3() {
                initHistoryList();
            }
        });
    }

    public void afterClickedBack(View view) {
        finish();
    }
}
