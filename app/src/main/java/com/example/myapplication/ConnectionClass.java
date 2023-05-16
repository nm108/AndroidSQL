package com.example.myapplication;

import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionClass {
    String ip = "192.168.1.103:1433";
    String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    String dbInstance = "AWDBINSTANCE";
    String db = "DBAW";
    String userName = "aw108";
    String password = "Firewall123";

    public Connection CONN() {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Connection conn = null;
        String ConnURL;

        try {
            Class.forName(driverClass);
            String url = "jdbc:jtds:sqlserver://"+ip+";instance="+dbInstance+";Database='"+db+"'";
            conn = DriverManager.getConnection(url, userName, password);
        }
        catch (SQLException se) {
            Log.e("Error 1: ", se.getMessage());
        }
        catch (ClassNotFoundException e) {
            Log.e("Error 3: ", e.getMessage());
        }
        catch (Exception e) {
            Log.e("Error 2: ", e.getMessage());
        }

        return conn;
    }
}