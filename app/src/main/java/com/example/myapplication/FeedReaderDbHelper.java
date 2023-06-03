package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;
import android.widget.EditText;

import androidx.core.text.StringKt;

import java.util.ArrayList;
import java.util.List;


public class FeedReaderDbHelper extends SQLiteOpenHelper {
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry._ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.IP_ADDRESS + " TEXT," +
                    FeedEntry.PORT + " TEXT," +
                    FeedEntry.DB_NAME + " TEXT," +
                    FeedEntry.DB_INSTANCE  + " TEXT," +
                    FeedEntry.USER_NAME + " TEXT," +
                    FeedEntry.PASSWORD + " TEXT)";

    private static final String SQL_POPULATE = "INSERT INTO " +
            FeedEntry.TABLE_NAME + "(" + FeedEntry._ID + ","+
            FeedEntry.IP_ADDRESS + "," +
            FeedEntry.PORT + "," +
            FeedEntry.DB_NAME + "," +
            FeedEntry.DB_INSTANCE + "," +
            FeedEntry.USER_NAME + "," +
            FeedEntry.PASSWORD +
            ") VALUES(1,NULL,NULL,NULL,NULL,NULL,NULL)";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    // private FeedReaderContract() {}



    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "DBConfig.db";

    public FeedReaderDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
        db.execSQL(SQL_POPULATE);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public List getDBConfigArray() {

        SQLiteDatabase db = getReadableDatabase();

        // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] projection = {
                FeedEntry._ID,
                FeedEntry.IP_ADDRESS,
                FeedEntry.PORT,
                FeedEntry.DB_NAME,
                FeedEntry.DB_INSTANCE,
                FeedEntry.USER_NAME,
                FeedEntry.PASSWORD
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = null;

        //FeedEntry.IP_ADDRESS + "," +
        //        FeedEntry.PORT + "," +
        //        FeedEntry.DB_NAME + "," +
        //        FeedEntry.DB_INSTANCE;
        String[] selectionArgs = null;

        Cursor cursor = db.query(
                FeedEntry.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                selection,              // The columns for the WHERE clause
                selectionArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null                    // The sort order
        );

        List result = new ArrayList();
        while(cursor.moveToNext()) {
            String ipAddress = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.IP_ADDRESS));
            result.add(ipAddress);
            String port = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.PORT));
            result.add(port);
            String dbName = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.DB_NAME));
            result.add(dbName);
            String dbInstance = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.DB_INSTANCE));
            result.add(dbInstance);
            String userName = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.USER_NAME));
            result.add(userName);
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(FeedEntry.PASSWORD));
            result.add(password);
        }
        cursor.close();
        Log.d("DB_TAG", "getFeedEntry: "+ result.toString());


        return result;
    }

    public void setDBConfigValue(String field, String value) {
        SQLiteDatabase db = getWritableDatabase();


// New value for one column
        ContentValues values = new ContentValues();
        values.put(field, value);

// Which row to update, based on the title
       String selection = FeedEntry._ID + " = ?";
       String[] selectionArgs = { "1" };

        int count = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    public void setDBConfigToDefault() {
        SQLiteDatabase db = getWritableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(FeedEntry.IP_ADDRESS, "null");
        values.put(FeedEntry.PORT, "null");
        values.put(FeedEntry.DB_NAME, "null");
        values.put(FeedEntry.DB_INSTANCE, "null");
        values.put(FeedEntry.USER_NAME, "null");
        values.put(FeedEntry.PASSWORD, "null");

// Which row to update, based on the title
        String selection = FeedEntry._ID + " = ?";
        String[] selectionArgs = { "1" };

        //String selection = null;
        //String selectionArgs[] = null;
        int count = db.update(
                FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

}
