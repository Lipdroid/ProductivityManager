package com.example.lipuhossain.productivitymanager.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.lipuhossain.productivitymanager.models.Schedule;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lipuhossain on 10/2/16.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "scheduleDatabase";

    // Table Names
    private static final String TABLE_SCHEDULE = "schedule";

    // TAGS Table - column names
    private static final String KEY_ID = "id";
    private static final String KEY_TAG_IN_TIME = "in_time";
    private static final String KEY_TAG_OUT_TIME = "out_time";
    private static final String KEY_TAG_SCHEDULE_NO = "schedule_no";
    private static final String KEY_TAG_BREAK_TIME = "break_time";
    private static final String KEY_TAG_TOTAL_TREATMENT_TIME = "total_treatment_time";
    private static final String KEY_TAG_TARGET_TREATMENT_TIME = "target_treatment_time";
    private static final String KEY_TAG_ACTUAL_TREATMENT_TIME = "actual_treatment_time";
    private static final String KEY_TAG_TARGET_PRODUCTIVITY = "target_productivity";
    private static final String KEY_TAG_ACTUAL_PRODUCTIVITY = "actual_productivity";
    private static final String KEY_TAG_TARGET_CLOCKOUT_TIME = "target_clockout_time";
    private static final String KEY_TAG_ACTUAL_CLOCKOUT_TIME = "actual_clockout_time";
    private static final String KEY_TAG_DATE = "date";

    private static final String KEY_TAG_WORKED_IN_THAT_SESSION = "working_hour";


    // Tag table create statement
    private static final String CREATE_TABLE_SCHEDULE = "CREATE TABLE " + TABLE_SCHEDULE
            + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_TAG_IN_TIME + " TEXT,"
            + KEY_TAG_OUT_TIME + " TEXT,"
            + KEY_TAG_SCHEDULE_NO + " TEXT,"
            + KEY_TAG_BREAK_TIME + " TEXT,"
            + KEY_TAG_TOTAL_TREATMENT_TIME + " TEXT,"
            + KEY_TAG_TARGET_TREATMENT_TIME + " TEXT,"
            + KEY_TAG_ACTUAL_TREATMENT_TIME + " TEXT,"
            + KEY_TAG_TARGET_PRODUCTIVITY + " TEXT,"
            + KEY_TAG_ACTUAL_PRODUCTIVITY + " TEXT,"
            + KEY_TAG_TARGET_CLOCKOUT_TIME + " TEXT,"
            + KEY_TAG_ACTUAL_CLOCKOUT_TIME + " TEXT,"
            + KEY_TAG_DATE + " TEXT,"
            + KEY_TAG_WORKED_IN_THAT_SESSION + " TEXT"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // creating required tables
        db.execSQL(CREATE_TABLE_SCHEDULE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + CREATE_TABLE_SCHEDULE);

        // create new tables
        onCreate(db);
    }

    /*
    * Creating a schedule
    */
    public long createSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_IN_TIME, schedule.getIn_time());
        values.put(KEY_TAG_OUT_TIME, schedule.getOut_time());
        values.put(KEY_TAG_BREAK_TIME, schedule.getBreak_time());
        values.put(KEY_TAG_SCHEDULE_NO, schedule.getSchedule_no());
        values.put(KEY_TAG_TOTAL_TREATMENT_TIME, schedule.getTotal_treatment_time());
        values.put(KEY_TAG_TARGET_TREATMENT_TIME, schedule.getTarget_treatment_time());
        values.put(KEY_TAG_ACTUAL_TREATMENT_TIME, schedule.getActual_treatment_time());
        values.put(KEY_TAG_TARGET_PRODUCTIVITY, schedule.getTarget_productivity());
        values.put(KEY_TAG_ACTUAL_PRODUCTIVITY, schedule.getActual_productivity());
        values.put(KEY_TAG_TARGET_CLOCKOUT_TIME, schedule.getTarget_clockout_time());
        values.put(KEY_TAG_ACTUAL_CLOCKOUT_TIME, schedule.getActual_clockout_time());
        values.put(KEY_TAG_DATE, schedule.getDate());
        values.put(KEY_TAG_WORKED_IN_THAT_SESSION, schedule.getWorked_in_that_session());
        // insert row
        long schedule_id = db.insert(TABLE_SCHEDULE, null, values);

        return schedule_id;
    }


    /*
    * get single schedule
    */
    public Schedule getSchedule(String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE + " WHERE "
                + KEY_TAG_DATE + " = " + date;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);

        if (c != null)
            c.moveToFirst();

        Schedule schedule = new Schedule();

        schedule.setDate(c.getString(c.getColumnIndex(KEY_TAG_DATE)));
        schedule.setId(Integer.toString(c.getInt(c.getColumnIndex(KEY_TAG_DATE))));
        schedule.setIn_time(c.getString(c.getColumnIndex(KEY_TAG_IN_TIME)));
        schedule.setOut_time(c.getString(c.getColumnIndex(KEY_TAG_OUT_TIME)));
        schedule.setBreak_time(c.getString(c.getColumnIndex(KEY_TAG_BREAK_TIME)));
        schedule.setSchedule_no(c.getString(c.getColumnIndex(KEY_TAG_SCHEDULE_NO)));
        schedule.setTotal_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TOTAL_TREATMENT_TIME)));
        schedule.setTarget_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_TREATMENT_TIME)));
        schedule.setActual_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_TREATMENT_TIME)));
        schedule.setTarget_productivity(c.getString(c.getColumnIndex(KEY_TAG_TARGET_PRODUCTIVITY)));
        schedule.setActual_productivity(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_PRODUCTIVITY)));
        schedule.setTarget_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_CLOCKOUT_TIME)));
        schedule.setActual_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_CLOCKOUT_TIME)));
        schedule.setWorked_in_that_session(c.getString(c.getColumnIndex(KEY_TAG_WORKED_IN_THAT_SESSION)));
        return schedule;
    }

    /*
    * getting all schedules
    * */
    public ArrayList<Schedule> getAllSchedules() {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        String selectQuery = "SELECT  * FROM " + TABLE_SCHEDULE;

        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();

                schedule.setDate(c.getString(c.getColumnIndex(KEY_TAG_DATE)));
                schedule.setId(Integer.toString(c.getInt(c.getColumnIndex(KEY_ID))));
                schedule.setIn_time(c.getString(c.getColumnIndex(KEY_TAG_IN_TIME)));
                schedule.setOut_time(c.getString(c.getColumnIndex(KEY_TAG_OUT_TIME)));
                schedule.setBreak_time(c.getString(c.getColumnIndex(KEY_TAG_BREAK_TIME)));
                schedule.setSchedule_no(c.getString(c.getColumnIndex(KEY_TAG_SCHEDULE_NO)));
                schedule.setTotal_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TOTAL_TREATMENT_TIME)));
                schedule.setTarget_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_TREATMENT_TIME)));
                schedule.setActual_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_TREATMENT_TIME)));
                schedule.setTarget_productivity(c.getString(c.getColumnIndex(KEY_TAG_TARGET_PRODUCTIVITY)));
                schedule.setActual_productivity(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_PRODUCTIVITY)));
                schedule.setTarget_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_CLOCKOUT_TIME)));
                schedule.setActual_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_CLOCKOUT_TIME)));
                schedule.setWorked_in_that_session(c.getString(c.getColumnIndex(KEY_TAG_WORKED_IN_THAT_SESSION)));
                // adding to todo list
                schedules.add(schedule);
            } while (c.moveToNext());
        }

        return schedules;
    }


    /*
    * getting all schedules by date
    * */
    public ArrayList<Schedule> getAllSchedulesByDate(String date) {
        ArrayList<Schedule> schedules = new ArrayList<Schedule>();

        String[] columns = {"*"};
        String selection = KEY_TAG_DATE + " =?";
        String[] selectionArgs = {date};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.query(TABLE_SCHEDULE, columns, selection, selectionArgs, null, null, null, null);

        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            do {
                Schedule schedule = new Schedule();

                schedule.setDate(c.getString(c.getColumnIndex(KEY_TAG_DATE)));
                schedule.setId(Integer.toString(c.getInt(c.getColumnIndex(KEY_ID))));
                schedule.setIn_time(c.getString(c.getColumnIndex(KEY_TAG_IN_TIME)));
                schedule.setOut_time(c.getString(c.getColumnIndex(KEY_TAG_OUT_TIME)));
                schedule.setBreak_time(c.getString(c.getColumnIndex(KEY_TAG_BREAK_TIME)));
                schedule.setSchedule_no(c.getString(c.getColumnIndex(KEY_TAG_SCHEDULE_NO)));
                schedule.setTotal_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TOTAL_TREATMENT_TIME)));
                schedule.setTarget_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_TREATMENT_TIME)));
                schedule.setActual_treatment_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_TREATMENT_TIME)));
                schedule.setTarget_productivity(c.getString(c.getColumnIndex(KEY_TAG_TARGET_PRODUCTIVITY)));
                schedule.setActual_productivity(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_PRODUCTIVITY)));
                schedule.setTarget_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_TARGET_CLOCKOUT_TIME)));
                schedule.setActual_clockout_time(c.getString(c.getColumnIndex(KEY_TAG_ACTUAL_CLOCKOUT_TIME)));
                schedule.setWorked_in_that_session(c.getString(c.getColumnIndex(KEY_TAG_WORKED_IN_THAT_SESSION)));

                // adding to todo list
                schedules.add(schedule);
            } while (c.moveToNext());
        }

        return schedules;
    }


    /*
    * Updating a todo
    */
    public int updateSchedule(Schedule schedule) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TAG_IN_TIME, schedule.getIn_time());
        values.put(KEY_TAG_OUT_TIME, schedule.getOut_time());
        values.put(KEY_TAG_BREAK_TIME, schedule.getBreak_time());
        values.put(KEY_TAG_SCHEDULE_NO, schedule.getSchedule_no());
        values.put(KEY_TAG_TOTAL_TREATMENT_TIME, schedule.getTotal_treatment_time());
        values.put(KEY_TAG_ACTUAL_TREATMENT_TIME, schedule.getActual_treatment_time());
        values.put(KEY_TAG_TARGET_PRODUCTIVITY, schedule.getTarget_productivity());
        values.put(KEY_TAG_ACTUAL_PRODUCTIVITY, schedule.getActual_productivity());
        values.put(KEY_TAG_TARGET_CLOCKOUT_TIME, schedule.getTarget_clockout_time());
        values.put(KEY_TAG_ACTUAL_CLOCKOUT_TIME, schedule.getActual_clockout_time());
        values.put(KEY_TAG_DATE, schedule.getDate());
        values.put(KEY_TAG_WORKED_IN_THAT_SESSION, schedule.getWorked_in_that_session());
        // updating row
        return db.update(TABLE_SCHEDULE, values, KEY_ID + " = ?",
                new String[]{String.valueOf(schedule.getId())});
    }


    /*
    * Deleting a todo
    */
    public void deleteSchedule(String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SCHEDULE, KEY_TAG_DATE + " = ?",
                new String[]{date});
    }


    // closing database
    public void closeDB() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db != null && db.isOpen())
            db.close();
    }

    public boolean CheckIsDataAlreadyInDBorNot(String date) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] columns = {"*"};
        String selection = KEY_TAG_DATE + " =?";
        String[] selectionArgs = {date};
        String limit = "1";

        Cursor cursor = db.query(TABLE_SCHEDULE, columns, selection, selectionArgs, null, null, null, limit);
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
