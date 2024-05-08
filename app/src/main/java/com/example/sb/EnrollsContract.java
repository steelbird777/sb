package com.example.sb;

import android.provider.BaseColumns;

public final class EnrollsContract {
    private EnrollsContract() {}

    public static class UserEntry implements BaseColumns {
        public static final String TABLE_NAME = "users";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_USERNAME = "username";
    }

    public static class EnrollmentEntry implements BaseColumns {
        public static final String TABLE_NAME = "enrollments";
        public static final String COLUMN_NAME_USER_ID = "user_id";
        public static final String COLUMN_NAME_SUBJECT_ID = "subject_id";
        public static final String COLUMN_NAME_SUBJECT_NAME = "subject_name";
        public static final String COLUMN_NAME_ENROLL_DATE = "enroll_date";
    }
}
