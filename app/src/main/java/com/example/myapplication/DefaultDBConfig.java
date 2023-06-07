package com.example.myapplication;

public class DefaultDBConfig {

    private final String defaultIpAddress = "192.168.1.103";
    private final String defaultPort = "1433";
    private final String defaultDBName = "DBAW";
    private final String defaultDBInstance = "AWDBINSTANCE";
    private final String defaultUserName = "?";
    private final String defaultPassword = "?";

    public String getDefaultIpAddress() {
        return defaultIpAddress;
    }

    public String getDefaultPort() {
        return defaultPort;
    }

    public String getDefaultDBName() {
        return defaultDBName;
    }

    public String getDefaultDBInstance() {
        return defaultDBInstance;
    }

    public String getDefaultUserName() {
        return defaultUserName;
    }

    public String getDefaultPassword() {
        return defaultPassword;
    }


}
