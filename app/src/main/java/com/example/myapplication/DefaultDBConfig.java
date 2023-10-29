package com.example.myapplication;

public class DefaultDBConfig {

    private final String defaultIpAddress = "sql.bsite.net";
    private final String defaultPort = "1433";
    private final String defaultDBName = "DBAW";
    private final String defaultDBInstance = "MSSQL2016";
    ;
    private final String defaultUserName = "nm108_awdb";
    private final String defaultPassword = "Firewall123";

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
