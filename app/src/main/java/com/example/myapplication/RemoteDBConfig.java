package com.example.myapplication;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import java.util.List;

public class RemoteDBConfig {
    private String defaultIpAddress = "192.168.1.103";
    private String defaultPort = "1433";
    private String defaultDBName = "DBAW";
    private String defaultDBInstance = "AWDBINSTANCE";

    private final String defaultUserName = "aw108";

    private final String defaultPassword = "Firewall123";

    private Context context;

    FeedReaderDbHelper dbHelper = new FeedReaderDbHelper(context);

    public RemoteDBConfig(final Context context) {
        this.context = context;
    }


    public String getIpAddress() {
        String result = getDbFieldValue(0);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultIpAddress;
        } else {
            return result;
        }
    }

    public void setDbConfigValue(Context context, View v, String name) {
        FeedReaderDbHelper frdbh = new FeedReaderDbHelper(context);
        EditText et = (EditText) v;

        frdbh.setDBConfigValue(name, et.getText().toString());
    }

    public void setDbConfigToDefault(Context context) {
        FeedReaderDbHelper frdbh = new FeedReaderDbHelper(context);
        frdbh.setDBConfigToDefault();
    }

    public String getPort() {

        String result = getDbFieldValue(1);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultPort;
        } else {
            return result;
        }
    }

    public String getDefaultDBName() {

        String result = getDbFieldValue(2);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultDBName;
        } else {
            return result;
        }
    }

    public String getDefaultDBInstance() {

        String result = getDbFieldValue(3);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultDBInstance;
        } else {
            return result;
        }
    }

    public String getDefaultUserName() {

        String result = getDbFieldValue(4);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultUserName;
        } else {
            return result;
        }
    }
    public String getDefaultPassword() {

        String result = getDbFieldValue(5);
        // add code that handles configuration written in local SQLite
        // Android database.
        if (result == null) {
            return defaultPassword;
        } else {
            return result;
        }
    }
    private String getDbFieldValue(int i) {
        FeedReaderDbHelper frdbh = new FeedReaderDbHelper(context);
        List entry = frdbh.getDBConfigArray();
        String result = (String) entry.get(i);
        if (result != null && result.equals("null")) {
            result = null;
        }
        return result;
    }
}
