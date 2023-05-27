package com.example.myapplication;

import android.provider.BaseColumns;

/* Inner class that defines the table contents */
public class FeedEntry implements BaseColumns {
    public static final String TABLE_NAME = "REMOTEDBCONFIGDATA";
    public static final String IP_ADDRESS = "IPADDRESS";
    public static final String PORT = "PORT";
    public static final String DB_NAME = "DB_NAME";
    public static final String DB_INSTANCE = "DB_INSTANCE";
}
