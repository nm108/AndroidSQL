package com.example.myapplication;

public class RemoteDBConfig {
    private String defaultIpAddress = "192.168.1.103";
    private String defaultPort = "1433";
    private String defaultDBName = "DBAW";
    private String defaultDBInstance = "AWDBINSTANCE";

    public String getIpAddress() {
        // add code that handles configuration written in local SQLite
        // Android database.
        return defaultIpAddress;
    }

    public String getPort() {
        // add code that handles configuration written in local SQLite
        // Android database.
        return defaultPort;
    }

    public String getDefaultDBName() {
        // add code that handles configuration written in local SQLite
        // Android database.
        return defaultDBName;
    }

    public String getDefaultDBInstance() {
        // add code that handles configuration written in local SQLite
        // Android database.
        return defaultDBInstance;
    }
}
