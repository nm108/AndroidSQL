package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Creates and maintains exactly one instance of Connection object, as in the
 * Singleton Design Pattern.
 *
 * @author Andrzej Wysocki
 */

public class DatabaseConnectionProviderSingleton {
    private static final String ip = "192.168.1.103:1433";
    private static final String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    private static final String dbInstance = "AWDBINSTANCE";
    private static final String db = "DBAW";
    private static final String userName = "aw108";
    private static final String password = "Firewall123";

    private static final String connURL = "jdbc:jtds:sqlserver://"+ip+";instance="+dbInstance+";Database='"+db+"'";

    private static Connection conn = null;


    static {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }
    public static Connection getConnection() {
        try {
            if (conn.isClosed()) { conn = null; }
        } catch (SQLException e) { conn = null; }

        if (conn == null ) {
            try {
                Class.forName(driverClass);
                conn = DriverManager.getConnection(connURL, userName, password);
            } catch (SQLException se) {
                Log.e("Error 1: ", se.getMessage());
            } catch (ClassNotFoundException e) {
                Log.e("Error 3: ", e.getMessage());
            } catch (Exception e) {
                Log.e("Error 2: ", e.getMessage());
            }
        }

        return conn;
    }
}
