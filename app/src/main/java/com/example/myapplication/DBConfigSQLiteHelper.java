package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.List;


public class DBConfigSQLiteHelper extends SQLiteOpenHelper {

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 4;
    public static final String DATABASE_NAME = "DBConfig.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBConfigEntry.TABLE_NAME + " (" +
                    DBConfigEntry._ID + " INTEGER PRIMARY KEY," +
                    DBConfigEntry.IP_ADDRESS + " TEXT," +
                    DBConfigEntry.PORT + " TEXT," +
                    DBConfigEntry.DB_NAME + " TEXT," +
                    DBConfigEntry.DB_INSTANCE  + " TEXT," +
                    DBConfigEntry.USER_NAME + " TEXT," +
                    DBConfigEntry.PASSWORD + " TEXT)";
    private static final String SQL_POPULATE = "INSERT INTO " +
            DBConfigEntry.TABLE_NAME + "(" + DBConfigEntry._ID + ","+
            DBConfigEntry.IP_ADDRESS + "," +
            DBConfigEntry.PORT + "," +
            DBConfigEntry.DB_NAME + "," +
            DBConfigEntry.DB_INSTANCE + "," +
            DBConfigEntry.USER_NAME + "," +
            DBConfigEntry.PASSWORD +
            ") VALUES(1,NULL,NULL,NULL,NULL,NULL,NULL)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBConfigEntry.TABLE_NAME;

    private DefaultDBConfig defaultConfigValues = new DefaultDBConfig();
    private Context context;

    public DBConfigSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public String getIpAddress() {
        String result = getDbFieldValue(0);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultIpAddress();
        } else {
            return result;
        }
    }

    public void setDbConfigValue(Context context, View v, String name) {
        EditText et = (EditText) v;
        setDBConfigValue(name, et.getText().toString());
    }

    public void setDbConfigToDefault(Context context) {
        SQLiteDatabase db = getWritableDatabase();

// New value for one column
        ContentValues values = new ContentValues();
        values.put(DBConfigEntry.IP_ADDRESS, "null");
        values.put(DBConfigEntry.PORT, "null");
        values.put(DBConfigEntry.DB_NAME, "null");
        values.put(DBConfigEntry.DB_INSTANCE, "null");
        values.put(DBConfigEntry.USER_NAME, "null");
        values.put(DBConfigEntry.PASSWORD, "null");

// Which row to update, based on the title
        String selection = DBConfigEntry._ID + " = ?";
        String[] selectionArgs = { "1" };

        //String selection = null;
        //String selectionArgs[] = null;
        int count = db.update(
                DBConfigEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

    }

    public String getPort() {

        String result = getDbFieldValue(1);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultPort();
        } else {
            return result;
        }
    }

    public String getDBName() {

        String result = getDbFieldValue(2);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultDBName();
        } else {
            return result;
        }
    }

    public String getDBInstance() {

        String result = getDbFieldValue(3);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultDBInstance();
        } else {
            return result;
        }
    }

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    // private FeedReaderContract() {}

    public String getUserName() {

        String result = getDbFieldValue(4);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultUserName();
        } else {
            return result;
        }
    }

    public String getPassword() {

        String result = getDbFieldValue(5);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultConfigValues.getDefaultPassword();
        } else {
            return result;
        }
    }

    private String getDbFieldValue(int i) {

        List entry = getDBConfigArray();
        String result = (String) entry.get(i);
        if (result != null && result.equals("null")) {
            result = null;
        }
        return result;
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
                DBConfigEntry._ID,
                DBConfigEntry.IP_ADDRESS,
                DBConfigEntry.PORT,
                DBConfigEntry.DB_NAME,
                DBConfigEntry.DB_INSTANCE,
                DBConfigEntry.USER_NAME,
                DBConfigEntry.PASSWORD
        };

        // Filter results WHERE "title" = 'My Title'
        String selection = null;

        //FeedEntry.IP_ADDRESS + "," +
        //        FeedEntry.PORT + "," +
        //        FeedEntry.DB_NAME + "," +
        //        FeedEntry.DB_INSTANCE;
        String[] selectionArgs = null;

        Cursor cursor = db.query(
                DBConfigEntry.TABLE_NAME,   // The table to query
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
                    cursor.getColumnIndexOrThrow(DBConfigEntry.IP_ADDRESS));
            result.add(ipAddress);
            String port = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBConfigEntry.PORT));
            result.add(port);
            String dbName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBConfigEntry.DB_NAME));
            result.add(dbName);
            String dbInstance = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBConfigEntry.DB_INSTANCE));
            result.add(dbInstance);
            String userName = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBConfigEntry.USER_NAME));
            result.add(userName);
            String password = cursor.getString(
                    cursor.getColumnIndexOrThrow(DBConfigEntry.PASSWORD));
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
       String selection = DBConfigEntry._ID + " = ?";
       String[] selectionArgs = { "1" };

        int count = db.update(
                DBConfigEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }




}
