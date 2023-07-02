package com.example.myapplication;

import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This is just prototype, replace it with production code.
 */
public class JDBCDatabaseHelper {

    private final String ip = "192.168.1.103:1433";
    private final String driverClass = "net.sourceforge.jtds.jdbc.Driver";
    private final String dbInstance = "AWDBINSTANCE";
    private final String db = "DBAW";
    private final String userName = "aw108";
    private final String password = "Firewall123";

    private String getConnURL(final String ip, final String dbInstance, final String db) {
        return "jdbc:jtds:sqlserver://"+ip+";instance="+dbInstance+";Database='"+db+"'";
    }

    private Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driverClass);
            conn = DriverManager.getConnection(getConnURL(ip, dbInstance, db), userName, password);
        } catch (SQLException se) {
            Log.e("Error 1: ", se.getMessage());
        } catch (ClassNotFoundException e) {
            Log.e("Error 3: ", e.getMessage());
        } catch (Exception e) {
            Log.e("Error 2: ", e.getMessage());
        }

        return conn;
    }

    // TODO: this is just prototype, replace it with code that uses database properly.
    // returned value also needs to be modified.
    private List doSelect() {
        Connection conn = getConnection();
        List result = new ArrayList();
        try {
            final Statement statement = conn.createStatement();
            final ResultSet rs = statement.executeQuery("SELECT * From USERS where UserName='"+ userName +"'");

            while (rs.next()) {
                result.add(rs.getString(0));
            }
        } catch (SQLException e) {
            Log.e("Error 4: ", e.getMessage());
        }

        return result;
    }
}
