package com.example.sb;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class EnrollsDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Enroll.db";

    private static final String SQL_CREATE_USERS_TABLE =
            "CREATE TABLE " + EnrollsContract.UserEntry.TABLE_NAME + " (" +
                    EnrollsContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    EnrollsContract.UserEntry.COLUMN_NAME_USER_ID + " TEXT," +
                    EnrollsContract.UserEntry.COLUMN_NAME_USERNAME + " TEXT)";

    private static final String SQL_CREATE_ENROLLMENTS_TABLE =
            "CREATE TABLE " + EnrollsContract.EnrollmentEntry.TABLE_NAME + " (" +
                    EnrollsContract.EnrollmentEntry._ID + " INTEGER PRIMARY KEY," +
                    EnrollsContract.EnrollmentEntry.COLUMN_NAME_USER_ID + " TEXT," +
                    EnrollsContract.EnrollmentEntry.COLUMN_NAME_SUBJECT_ID + " TEXT," +
                    EnrollsContract.EnrollmentEntry.COLUMN_NAME_SUBJECT_NAME + " TEXT," +
                    EnrollsContract.EnrollmentEntry.COLUMN_NAME_ENROLL_DATE + " TEXT)";

    private static final String SQL_DELETE_USERS_TABLE =
            "DROP TABLE IF EXISTS " + EnrollsContract.UserEntry.TABLE_NAME;

    private static final String SQL_DELETE_ENROLLMENTS_TABLE =
            "DROP TABLE IF EXISTS " + EnrollsContract.EnrollmentEntry.TABLE_NAME;

    public EnrollsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USERS_TABLE);
        db.execSQL(SQL_CREATE_ENROLLMENTS_TABLE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS_TABLE);
        db.execSQL(SQL_DELETE_ENROLLMENTS_TABLE);
        onCreate(db);
    }
}
