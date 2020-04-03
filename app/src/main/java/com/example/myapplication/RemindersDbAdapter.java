package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class RemindersDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "RemindersDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_remdrs";
    private static final String TABLE_NAME = "tbl_remdrs";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public RemindersDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    // implement the function createReminder() which take the name as the content of the reminder and boolean important...note that the id will be created for you automatically
    public void createReminder(String content, boolean important) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, content);
        int impInt = important ? 1 : 0;
        values.put(COL_IMPORTANT, impInt);
        mDb.insert(TABLE_NAME, null, values);
    }
    // overloaded to take a reminder
    public long createReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());
        long id = mDb.insert(TABLE_NAME, null, values);
        reminder.setId(id);
        return id;
    }

    // implement the function fetchReminderById() to get a certain reminder given its id
    public Reminder fetchReminderById(int id) {
        Cursor cursor = mDb.query(
                TABLE_NAME,
                null,
                COL_ID + "=?",
                new String[] {String.valueOf(id)},
                null,
                null,
                null
        );
        cursor.moveToFirst();
        return new Reminder(
                cursor.getLong(INDEX_ID),
                cursor.getString(INDEX_CONTENT),
                cursor.getInt(INDEX_IMPORTANT)
        );
    }


    // implement the function fetchAllReminders() which get all reminders
    public Cursor fetchAllReminders() {
        String selectQuery = "SELECT * FROM " +TABLE_NAME;
        return mDb.rawQuery(selectQuery, null);
    }

    // implement the function updateReminder() to update a certain reminder
    public void updateReminder(Reminder reminder) {
        ContentValues values = new ContentValues();
        values.put(COL_CONTENT, reminder.getContent());
        values.put(COL_IMPORTANT, reminder.getImportant());
        mDb.update(
                TABLE_NAME,
                values,
                COL_ID+"=?",
                new String[] {String.valueOf(reminder.getId())}
        );
    }
    // implement the function deleteReminderById() to delete a certain reminder given its id
    public void deleteReminderById(int nId) {
        mDb.delete(
                TABLE_NAME,
                COL_ID +"=?",
                new String[] {String.valueOf(nId)}
        );
    }

    // implement the function deleteAllReminders() to delete all reminders
    public void deleteAllReminders() {
        mDb.delete(TABLE_NAME, null, null);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
