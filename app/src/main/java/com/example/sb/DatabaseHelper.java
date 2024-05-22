package com.example.sb;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "userDetails.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_NAME = "user";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_EMAIL_ID = "email_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_LOGIN_TIME = "login_time";
    public static final String COLUMN_LOGOUT_TIME = "logout_time";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_EMAIL_ID + " TEXT, " +
                    COLUMN_USER_NAME + " TEXT, " +
                    COLUMN_LOGIN_TIME + " INTEGER, " +
                    COLUMN_LOGOUT_TIME + " INTEGER);";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertLoginDetails(String emailId, String userName, long loginTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_EMAIL_ID, emailId);
        values.put(COLUMN_USER_NAME, userName);
        values.put(COLUMN_LOGIN_TIME, loginTime);

        db.insert(TABLE_NAME, null, values);
    }

    public void updateLogoutTime(String emailId, long logoutTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOGOUT_TIME, logoutTime);

        String selection = COLUMN_EMAIL_ID + " = ?";
        String[] selectionArgs = {emailId};

        db.update(TABLE_NAME, values, selection, selectionArgs);
    }

    public Cursor getUserDetails(String emailId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String[] projection = {
                COLUMN_EMAIL_ID,
                COLUMN_USER_NAME,
                COLUMN_LOGIN_TIME,
                COLUMN_LOGOUT_TIME
        };

        String selection = COLUMN_EMAIL_ID + " = ?";
        String[] selectionArgs = {emailId};

        return db.query(
                TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
    }
}
