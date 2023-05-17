package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    String ip = "192.168.1.103:1433";
    String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    String dbInstance = "AWDBINSTANCE";
    String db = "DBAW";
    String userName = "aw108";
    String password = "Firewall123";

    String connURL = "jdbc:jtds:sqlserver://"+ip+";instance="+dbInstance+";Database='"+db+"'";

    Connection conn = null;

    public Connection getConnection() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        if (conn == null) {
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