package com.example.sb;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DatabaseViewerActivity extends AppCompatActivity {

    private ListView databaseListView;
    private DatabaseAdapter databaseAdapter;
    private SQLiteDatabase sqLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database_viewer);

        databaseListView = findViewById(R.id.databaseListView);
        databaseAdapter = new DatabaseAdapter(this, null);
        databaseListView.setAdapter(databaseAdapter);

        // Open the SQLite database
        SQLiteOpenHelper dbHelper = new EnrollsDbHelper(this);
        sqLiteDatabase = dbHelper.getReadableDatabase();

        // Query the database and update the UI
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                EnrollsContract.EnrollmentEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );
        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Extract data from the cursor and log it
                String userId = cursor.getString(cursor.getColumnIndexOrThrow(EnrollsContract.UserEntry.COLUMN_NAME_USER_ID));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(EnrollsContract.UserEntry.COLUMN_NAME_USERNAME));
                Log.d("Database", "User ID: " + userId + ", Username: " + username);
            } while (cursor.moveToNext());
            cursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close the Cursor and database when the activity is destroyed
        databaseAdapter.changeCursor(null);
        sqLiteDatabase.close();
    }

    public class DatabaseAdapter extends CursorAdapter {

        public DatabaseAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return null;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView textViewData = view.findViewById(R.id.textViewData);

            // Extract data from the cursor and set it to the TextView
            String data = cursor.getString(cursor.getColumnIndexOrThrow(EnrollsContract.UserEntry.COLUMN_NAME_USER_ID));
            textViewData.setText(data);
        }


    }
}
